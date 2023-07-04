#!/bin/bash

cd ..
git checkout student
cd ..
tar --exclude='can_train/.git' --exclude='can_train/.gitignore' -czvf vehicle_hacking_training.tar.gz can_train/
cd can_train
git checkout main
cd /home/kali/Documents/can_train
