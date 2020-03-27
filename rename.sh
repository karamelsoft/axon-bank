#!/usr/bin/env bash

for folder in `find . -type d -name karamelsoft`
do
  new_folder=$(echo $folder | sed -e "s/karamelsoft\$/karamelsoft/")

  mv -v $folder $new_folder
done
