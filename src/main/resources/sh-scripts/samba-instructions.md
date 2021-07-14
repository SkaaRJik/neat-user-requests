# Samba install instructions

## Linux (Debian based)

Firstly:

`sudo apt install -y samba`

If the first step doesnt work, try:

`sudo apt install -y samba-common python-glade2 system-config-samba samba`

Backup samba config file:

`sudo cp /etc/samba/smb.conf{,.bak}`

Open config with text editor
 
`sudo nano /etc/samba/smb.conf`
 
Paste this:

```
[global]
workgroup = WORKGROUP
netbios name = NEAT
server string = SambaServer
dns proxy = no
log file = /var/log/samba/log.%m
max log size = 1000
map to guest = bad user
usershare allow guests = yes

[NEAT]
comment = NEAT Input data store
path = /samba/neat-store
valid users = victor
read only = no
browsable = yes
writable = yes
```

Create network shared directory:

`mkdir /samba`

`mkdir /samba/neat-store`

Change access rules:

`sudo chmod -R 777 /samba`

Change owner for directory:

`sudo chown -R $USER@$USER /samba`

Add current linux user to samba (Password required)

`sudo smbpasswd -a $USER`
 
Activate new samba user:

`sudo smbpasswd -e $USER`

Restart Samba service

`systemctl restart smbd`

