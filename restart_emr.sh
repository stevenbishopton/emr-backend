#!/bin/bash

echo "🔄 Restarting EMR Application..."

# Kill any existing Java processes
pkill -f "emr" || echo "No existing EMR processes found"

# Wait a moment
sleep 2

# Start the application
echo "🚀 Starting EMR with fresh database..."
cd /home/thealchemist/Downloads/emr
./mvn spring-boot:run

echo "✅ EMR Application started!"
echo "🌐 Available at: http://localhost:8080"
echo "🗄️ H2 Console: http://localhost:8080/h2-console"
