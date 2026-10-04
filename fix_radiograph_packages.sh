#!/bin/bash

echo "🔧 Fixing Radiograph Package Structure..."

# Create repos directory if it doesn't exist
mkdir -p src/main/java/hospital/emr/radiograph/repos

# Move repository files to repos package
echo "📁 Moving repository files..."
mv src/main/java/hospital/emr/radiograph/repository/* src/main/java/hospital/emr/radiograph/repos/

# Update package declarations
echo "📝 Updating package declarations..."

# Update RadiographRepository.java
sed -i 's/package hospital.emr.radiograph.repository;/package hospital.emr.radiograph.repos;/' \
    src/main/java/hospital/emr/radiograph/repos/RadiographRepository.java

# Update service imports
sed -i 's/import hospital.emr.radiograph.repository.RadiographRepository;/import hospital.emr.radiograph.repos.RadiographRepository;/' \
    src/main/java/hospital/emr/radiograph/services/RadiographServiceImpl.java

# Remove empty repository directory
rmdir src/main/java/hospital/emr/radiograph/repository

echo "✅ Package structure fixed!"
echo "🚀 You can now rebuild and restart the application"
