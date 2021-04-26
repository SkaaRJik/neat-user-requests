sudo apt update && sudo apt install samba && sudo smbpasswd -a $USER &&
mkdir /home/$USER/neat-store &&
sudo touch /etc/samba/smb.conf && sudo chown $USER:$USER /etc/samba/smb.conf && echo -e "[global]\nworkgroup = WORKGROUP\nnetbios name = NEAT\n\n[NEAT-folder]\ncomment = NEAT Input data store\npath = /home/"$USER"/neat-store\nvalid users = "$USER"\nread only = no\nbrowsable = yes" >> /etc/samba/smb.conf &&
sudo systemctl start smb.service && sudo systemctl enable smb.service &&
ip a



