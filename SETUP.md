# Challenge Setup

1. Get a Ubuntu VM
2. Install tooling
    ```
    curl -fsSL https://get.docker.com -o get-docker.sh
    sudo sh ./get-docker.sh
    sudo apt install tmux vim
    echo "127.0.0.1 kafka" | sudo tee -a /etc/hosts
    ```
3. Clone the repo
    ```
    git clone --depth 1 -b challenge https://github.com/papaux/dns-challenge.git
    ```
