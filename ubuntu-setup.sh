#!/bin/bash

# Setup interview challenge environment on Ubuntu

# Input for github password
read -s -p "Enter code-server password: " CS_PASSWORD

# Install docker and helper packages
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh ./get-docker.sh
sudo apt install -y tmux vim zsh openjdk-21-jdk kafkacat
echo "127.0.0.1 kafka" | sudo tee -a /etc/hosts
sudo gpasswd -a $USER docker

# Setup the challenge repository and clean
git config --global user.email "challenge@example.com"
git config --global user.name "DNS Challenge"
git checkout challenge
rm -rf .git
git init
git add .
git commit -m "Starting point for DNS challenge"

sudo docker compose build
sudo docker compose pull

mkdir -p ~/.config/code-server
cat > ~/.config/code-server/config.yaml << EOF
bind-addr: 0.0.0.0:8080
auth: password
password: $CS_PASSWORD
cert: false
EOF

curl -fsSL https://code-server.dev/install.sh | sh

# install extensions
code-server --install-extension redhat.java
code-server --install-extension vscjava.vscode-gradle

sudo systemctl enable --now code-server@$USER

echo "Setup complete!"
