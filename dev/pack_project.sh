#!/bin/bash

cd ..
git checkout student
tar --exclude='.git' --exclude='.gitignore' -czvf ../vehicle_hacking_training.tar.gz ./
git checkout main
cd /home/kali/Documents/can_train
