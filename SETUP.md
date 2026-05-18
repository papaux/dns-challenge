# Challenge Setup

1. Get a Ubuntu VM
2. Install tooling
    ```
    curl -fsSL https://get.docker.com -o get-docker.sh
    sudo sh ./get-docker.sh
    sudo apt install -y tmux vim zsh openjdk-21-jdk
    echo "127.0.0.1 kafka" | sudo tee -a /etc/hosts
    sudo gpasswd -a $USER docker
    newgrp docker
    ```
3. Get a personal access token giving access to this repo only
4. Clone the repo
    ```
    git clone --depth 1 -b challenge https://github.com/papaux/dns-challenge.git
    ```
5. Remove git link and create an empty repo
    ```
    rm -rf .git
    git init
    git add .
    git commit -m "Starting point"
    ```
6. Install vscode
    ```
    curl -fsSL https://code-server.dev/install.sh | sh
    ```
7. Configure (edit the password)
    
    `cat ~/.config/code-server/config.yaml`
    
    ```
    bind-addr: 0.0.0.0:8080
    auth: password
    password: <PASSWORD>
    cert: false
    ```
8. Run
    ```
    code-server /home/cloud_user/dns-challenge/
    ```
9. Connect and install extensions
   1.  Java
10. Run the docker images once
    ```
    docker compose up --build
    ```

