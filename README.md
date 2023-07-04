# Before Starting

Read at least the first 2 parts of this tutorial series:
[Car Hacking 101](https://medium.com/@yogeshojha/car-hacking-101-practical-guide-to-exploiting-can-bus-using-instrument-cluster-simulator-part-i-cd88d3eb4a53)

# Setup / Installation

## Requirements
Docker
canutils
python-can

## First Use

# Usage
Run `python3 vcs_vehicle_hacking.py`

## Root access
The Python interface requires sudo access to run docker commands in the background. This may result in a prompt for you password during execution.

The training will not function properly without this access.

# Useful (if not required) tools to have and know for the training
canutils
jadx or jd-gui
javac
file
binwalk

# Known Issues
On first run of each mission, the Docker container will be built. This can take quite a while. After running the first time, it should be faster.
