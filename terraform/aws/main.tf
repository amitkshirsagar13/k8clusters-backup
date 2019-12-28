provider "aws" {
  profile                 = "terraform"
  region                  = "ap-southeast-1"
  shared_credentials_file = ".aws/credentials"
}

data "aws_vpc" "main" {
  id = "vpc-0e20d3b890c8edfba"
}

data "aws_internet_gateway" "gw" {
  internet_gateway_id = "igw-026b2861ab3287ba0"
}

data "aws_availability_zone" "linux" {
  name = "ap-southeast-1a"
}

data "aws_availability_zone" "windows" {
  name = "ap-southeast-1b"
}

resource "aws_vpn_gateway" "vpn" {
  tags = {
    Name = "main-vpn-gateway"
  }
}

data "aws_iam_instance_profile" "ssm" {
  name = "AmazonSSMRoleForInstancesQuickSetup"
}

resource "aws_route_table" "main_route" {
  vpc_id = "${data.aws_vpc.main.id}"
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${data.aws_internet_gateway.gw.id}"
  }
  tags = {
    Name = "publicRouteTable"
  }
}

resource "aws_vpn_gateway_attachment" "vpn_attachment" {
  vpc_id         = "${data.aws_vpc.main.id}"
  vpn_gateway_id = "${aws_vpn_gateway.vpn.id}"
}

resource "aws_subnet" "sn_linux" {
  vpc_id     = "${data.aws_vpc.main.id}"
  cidr_block = "172.31.50.0/24"
  availability_zone = "${data.aws_availability_zone.linux.name}"
  tags = {
    Name = "sn_linux"
  }
}

resource "aws_subnet" "sn_windows" {
  vpc_id     = "${data.aws_vpc.main.id}"
  cidr_block = "172.31.60.0/24"
  availability_zone = "${data.aws_availability_zone.windows.name}"
  tags = {
    Name = "sn_windows"
  }
}

resource "aws_security_group" "linux" {
    name = "vpc_linux"
    description = "Allow traffic to pass from the private subnet to the internet"
    ingress {
        from_port = 80
        to_port = 80
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }
    ingress {
        from_port = 22
        to_port = 22
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }
    egress {
      from_port       = 0
      to_port         = 0
      protocol        = "-1"
      cidr_blocks     = ["0.0.0.0/0"]
    }
    vpc_id = "${data.aws_vpc.main.id}"
}

resource "aws_security_group" "windows" {
    name = "vpc_windows"
    description = "Allow traffic to pass from the private subnet to the internet"

    ingress {
        from_port = 80
        to_port = 80
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }
    ingress {
      from_port   = 5985
      to_port     = 5985
      protocol    = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
    }
    ingress {
      from_port   = 3389
      to_port     = 3389
      protocol    = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
    }
    egress {
      from_port       = 0
      to_port         = 0
      protocol        = "-1"
      cidr_blocks     = ["0.0.0.0/0"]
    }
    vpc_id = "${data.aws_vpc.main.id}"
}

resource "aws_s3_bucket" "images" {
  bucket = "s3-amit-kshirsagar-13"
  acl    = "public-read"
}

resource "aws_eip" "windows" {
    instance = "${aws_instance.windows.id}"
    vpc = true
}

resource "aws_eip" "linux" {
    instance = "${aws_instance.linux.id}"
    vpc = true
}

resource "aws_instance" "linux" {
  ami           = "ami-061eb2b23f9f8839c"
  instance_type = "t2.micro"
  availability_zone = "${data.aws_availability_zone.linux.name}"
  subnet_id = "${aws_subnet.sn_linux.id}"
  security_groups = ["${aws_security_group.linux.id}"]
  iam_instance_profile = "${data.aws_iam_instance_profile.ssm.name}"

  key_name      = "ec2"

  tags = {
    Name = "HelloWorld-Linux"
  }
}

resource "null_resource" "linux_provisioner" {
  connection {
    host        = "${aws_eip.linux.public_ip}"
    type        = "ssh"
    user        = "ubuntu"
    private_key = "${file("ec2.pem")}"
  }
  
  provisioner "file" {
    source      = "default.conf"
    destination = "/tmp/default.conf"
  }

  provisioner "file" {
    source      = "index.linux.html"
    destination = "/tmp/index.html"
  }

  provisioner "file" {
    source      = "script.sh"
    destination = "/tmp/script.sh"
  }

  provisioner "remote-exec" {
    inline = [
      "chmod +x /tmp/script.sh",
      "/tmp/script.sh",
    ]
  }
  depends_on = [aws_volume_attachment.ebs_linux_attachment]
}

resource "aws_eip_association" "eip_assoc" {
  instance_id   = "${aws_instance.linux.id}"
  allocation_id = "${aws_eip.linux.id}"
  depends_on = [aws_instance.linux]
}

resource "aws_ebs_volume" "ebs_linux" {
  availability_zone = "${data.aws_availability_zone.linux.name}"
  size              = 1
  tags = {
    Name = "Linux"
  }
}

resource "aws_volume_attachment" "ebs_linux_attachment" {
  device_name = "/dev/sdh"
  volume_id   = "${aws_ebs_volume.ebs_linux.id}"
  instance_id = "${aws_instance.linux.id}"
  depends_on = [aws_eip_association.eip_assoc]
}

resource "aws_instance" "windows" {
  ami = "ami-0231f120d90c28482"
  instance_type = "t2.micro"

  availability_zone = "${data.aws_availability_zone.windows.name}"
  subnet_id = "${aws_subnet.sn_windows.id}"
  security_groups = ["${aws_security_group.windows.id}"]
  iam_instance_profile = "${data.aws_iam_instance_profile.ssm.name}"
  key_name      = "ec2"

  tags = {
    Name = "HelloWorld-Windows"
  }
  get_password_data      = true
  user_data = <<EOF
  <script>
    winrm quickconfig -q & winrm set winrm/config @{MaxTimeoutms="1800000"} & winrm set winrm/config/service @{AllowUnencrypted="true"} & winrm set winrm/config/service/auth @{Basic="true"}
  </script>
  <powershell>
    netsh advfirewall firewall add rule name="WinRM in" protocol=TCP dir=in profile=any localport=5985 remoteip=any localip=any action=allow
    $admin = [adsi]("WinNT://./administrator, user")
    $admin.psbase.invoke("SetPassword", "password")
  </powershell>
  EOF
}

resource "null_resource" "windows_provisioner" {
  connection {
    host        = "${aws_eip.windows.public_ip}"
    type     = "winrm"
    user     = "Administrator"
    password = "${rsadecrypt(aws_instance.windows.password_data, file("ec2.pem"))}"

    # set from default of 5m to 10m to avoid winrm timeout
    timeout = "6m"
  }
  
  provisioner "file" {
    source      = "script.ps1"
    destination = "C:/script.ps1"
  }
  
  provisioner "file" {
    source      = "diskpart.txt"
    destination = "C:/diskpart.txt"
  }
  
  provisioner "file" {
    source      = "index.windows.html"
    destination = "C:/index.html"
  }

  provisioner "remote-exec" {
    inline = [
      "powershell C:/script.ps1",
    ]
  }
  depends_on = [aws_volume_attachment.ebs_windows_attachment]
}

resource "aws_ebs_volume" "ebs_windows" {
  availability_zone = "${data.aws_availability_zone.windows.name}"
  size              = 1
  tags = {
    Name = "Windows"
  }
}

resource "aws_volume_attachment" "ebs_windows_attachment" {
  device_name  = "xvdj"
  volume_id    = "${aws_ebs_volume.ebs_windows.id}"
  instance_id  = "${aws_instance.windows.id}"
  skip_destroy = true
  depends_on = [aws_eip_association.eip_windows]
}

resource "aws_eip_association" "eip_windows" {
  instance_id   = "${aws_instance.windows.id}"
  allocation_id = "${aws_eip.windows.id}"
  depends_on = [aws_instance.windows]
}

resource "aws_elb" "lb" {
  name               = "lb-elb"
  # availability_zones = ["${data.aws_availability_zone.linux.name}", "${data.aws_availability_zone.windows.name}"]
  subnets            = ["${aws_subnet.sn_linux.id}", "${aws_subnet.sn_windows.id}"]

  access_logs {
    bucket        = "logs"
    bucket_prefix = "access"
    interval      = 60
  }

  listener {
    instance_port     = 80
    instance_protocol = "http"
    lb_port           = 80
    lb_protocol       = "http"
  }
  

  health_check {
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 3
    target              = "HTTP:80/"
    interval            = 30
  }

  instances                   = ["${aws_instance.linux.id}", "${aws_instance.windows.id}"]
  cross_zone_load_balancing   = true
  idle_timeout                = 400
  connection_draining         = true
  connection_draining_timeout = 400

  tags = {
    Name = "HelloWorld-elb"
  }
}

output "ec2_password" {
  value = "${rsadecrypt(aws_instance.windows.password_data, file("ec2.pem"))}"
}


output "vpc_cdir" {
  value = "${data.aws_vpc.main.cidr_block}"
}