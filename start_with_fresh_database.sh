#!/bin/bash

echo "🚀 Starting EMR with Fresh Database..."

# Change to project directory
cd /home/thealchemist/Downloads/emr

# Check if database exists, if not create fresh one
if [ ! -f "../emr_database.mv.db" ]; then
    echo "📁 No database found - will create fresh schema"
else
    echo "📁 Database exists - using existing schema"
fi

# Start the application
echo "🔧 Starting Spring Boot application..."
./mvn spring-boot:run

echo "✅ Application started successfully!"
echo "🌐 Available at: http://localhost:8080"
echo "🗄️ H2 Console: http://localhost:8080/h2-console"
