# Tartan

## Current testing coverage
### Full Report
The full report can be found in Platform/build/jacocoHtml/index.html after running the tests
### Statements:
![Coverage](.github/badges/jacoco.svg)

### Branches
![Branches](.github/badges/branches.svg)

## Continuous Deployment
  
  ### Check if Runners are Online

  Self-hosted runners may go offline on computer restart. Check the runners through these steps:

  Actions --> Management/Runners (Left Sidebar) --> New Runners (Right Button)
  ![image](https://github.com/cmput402/group-project-402monday3/assets/73728946/df499820-7209-41c0-916d-079db9900da1)

  Note that only github-runner2 is needed to be online. The deployment fails when run on 402Monday3-vm at the moment. Hard-Reboot 402Monday3-vm on Cybera to return it back to offline.

  If github-runner2 is offline, use these commands in the terminal to access the VM and start the runner:  (REQUIRES 402-proj.key)

    cd ~/.ssh
    ssh -i 402-proj.key ubuntu@2605:fd00:4:1001:f816:3eff:fe07:c05d
    sudo su runner
    cd ~/actions-runner
    nohup ./run.sh &

    exit
    exit

  ### Reverting Versions 

  In the .github/ directory, run: 

    ./revert.sh

  If permissions required, run:

    chmod +x revert.sh

  ### Manual Deployment  

  In the smart-home/Platform/ directory run:

    docker-compose up --build

  If you have made changes and want your docker setup to reflect it, run

     docker-compose down && docker-compose build --no-cache && docker-compose up

  ### Updating Containers After Build

  In the smart-home/Platform/ directory run:

    docker-compose build

    docker-compose down

    docker-compose up -d

## Building

The build instructions can be found [here](./docs/build_instructions.md).


## System description

System description can be downloaded as a pdf file
[here](./docs/TartanSystemDescription.pdf).

## Folder structure

The entire system (Tartan Smart Home Service) resides in *smart-home/*
directory.

Please see the system description (docx) file for more detailed information
about Tartan's design, architecture, requirements, etc.
