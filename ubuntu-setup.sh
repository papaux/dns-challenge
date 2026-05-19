#!/bin/bash

# Setup interview challenge environment on Ubuntu

# Input for github password
read -sp "Enter your GitHub User: " GITHUB_USER
read -sp "Enter your GitHub Token: " GITHUB_TOKEN
read -s -p "Enter code-server password: " CS_PASSWORD

# Install docker and helper packages
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh ./get-docker.sh
sudo apt install -y tmux vim zsh openjdk-21-jdk
echo "127.0.0.1 kafka" | sudo tee -a /etc/hosts
sudo gpasswd -a $USER docker
newgrp docker

git clone --depth 1 -b challenge https://$GITHUB_USER:$GITHUB_TOKEN@github.com/papaux/dns-challenge.git

cd dns-challenge
rm -rf .git
git init
git add .
git commit -m "Starting point for DNS challenge"

docker compose build

curl -fsSL https://code-server.dev/install.sh | sh

mkdir -p ~/.config/code-server
cat > ~/.config/code-server/config.yaml << EOF
bind-addr: 0.0.0.0:8080
auth: password
password: $CS_PASSWORD
cert: false
EOF

code-server /home/$USER/dns-challenge &

echo "Setup complete!"
