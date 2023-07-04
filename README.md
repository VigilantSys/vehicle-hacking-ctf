# Before Starting

Read at least the first 2 parts of this tutorial series:
[Car Hacking 101](https://medium.com/@yogeshojha/car-hacking-101-practical-guide-to-exploiting-can-bus-using-instrument-cluster-simulator-part-i-cd88d3eb4a53)

# Setup / Installation

Run 
```shell
$ chmod +x install
$ ./install
```

# Usage
Run `python3 vcs_vehicle_hacking.py`

## Root access
The Python interface requires sudo access to run docker commands in the background. This may result in a prompt for your password during execution.

The training will not function properly without this access.

# Useful tools to have and know for the training
jadx or jd-gui
recaf
javac
file
binwalk
7z
can-utils

# Known Issues
During mission 3, their is sometimes a bug where the network times out and will not reconnect. Restart the program with <CTRL>+C if this occurs.

On first run of each mission, the Docker container will be built. This can take quite a while, particularly on mission1. After running the first time, it should be faster.
