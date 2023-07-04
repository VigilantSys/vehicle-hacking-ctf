#!/bin/bash

# Create manifest file
sha1=$(sha1sum ../mission2/firmware.jar)
echo "${sha1/..\/mission2\//}" > ../mission2/manifest.txt

# Obfuscate firmware file
echo "/*****************************************/" > /tmp/comment.txt
echo "/* Created and owned by the consortium   */" >> /tmp/comment.txt
echo "/* of fictional corporations - 2018.     */" >> /tmp/comment.txt
echo "/* Very few rights reserved, but don't   */" >> /tmp/comment.txt
echo "/* blame us if you steal our stuff and   */" >> /tmp/comment.txt
echo "/* then it has bugs. That's your problem */" >> /tmp/comment.txt
echo "/*****************************************/" >> /tmp/comment.txt
cat /tmp/comment.txt ../mission2/firmware.jar > ../mission2/firmware

# Remove extra files
rm ../mission2/firmware.jar

# Create .iso file
mkisofs -o ../mission2/roylls_roce_ghoul_vii_firmware_update_2018 ../mission2/

# Clean up
rm /tmp/comment.txt
rm ../mission2/firmware
rm ../mission2/manifest.txt
