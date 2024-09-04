#!/bin/bash

# Get list of packages from package.json
TYPES="dependencies devDependencies"
for TYPE in $TYPES; do
    rm -f _report_latest_npm_versions-$TYPE.txt
    echo '{' > _report_latest_npm_versions-$TYPE.json
    packages=$(jq -r ".$TYPE | keys[]" src/main/ui/package.json)

    # Iterate over each package
    for package in $packages; do
        # Remove quotes from package name
        package="${package%\"}"
        package="${package#\"}"
        
        echo "Checking latest version of $package" | tee -a _report_latest_npm_versions-$TYPE.txt
        # Fetch the latest version of the package
        version=$(npm show $package version)
        
        echo "  Latest version of $package is $version" | tee -a _report_latest_npm_versions-$TYPE.txt
        echo "  \"$package\": \"$version\"," | tee -a _report_latest_npm_versions-$TYPE.json
    done

    echo '}' | tee -a _report_latest_npm_versions-$TYPE.json
done
