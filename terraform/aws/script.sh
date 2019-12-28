#!/bin/bash
format=`sudo blkid -o value -s TYPE /dev/xvdh`
echo $format
if [ $format != 'ext4' ]
then
sudo mkfs.ext4 /dev/xvdh
fi
sudo mkdir -p /storage
sudo chmod 777 -R /storage
sudo mount /dev/xvdh /storage/
sudo DEBIAN_FRONTEND=noninteractive apt-get -yq install nginx
sudo systemctl start nginx
sudo cp /tmp/default.conf /etc/nginx/sites-enabled/default
sudo systemctl restart nginx
sudo mkdir -p /storage/src
sudo cp /tmp/index.html /storage/src/index.html