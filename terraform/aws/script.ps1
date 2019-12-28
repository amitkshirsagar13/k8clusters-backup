Import-Module WebAdministration
DiskPart /s C:\diskpart.txt

New-Item -Path "D:\" -Name "src" -ItemType "directory"
Move-Item -Path C:\index.html -Destination D:\src\index.html

Install-WindowsFeature -name Web-Server -IncludeManagementTools
[string] $iisWebsiteName = 'Default Web Site'

$iisWebsite = Get-WebFilePath "IIS:\Sites\$iisWebsiteName"

Set-ItemProperty -Path "IIS:\Sites\$iisWebsiteName" -Name PhysicalPath -Value "D:\src"
IISReset /RESTART
Set-ItemProperty -Path "IIS:\Sites\$iisWebsiteName" -Name PhysicalPath -Value "D:\src"
IISReset /RESTART

if($iisWebsite -ne $null)
{ 
    Write-Output "Website's physical path:"
    Write-Output $iisWebsite.FullName
}
else
{
    Write-Output "Website not found"
}   