#!/bin/bash

# Wordium Project Initialization Script
# This script automates the setup of environment variables for all microservices.

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}==========================================================${NC}"
echo -e "${BLUE}          🚀 Wordium: Microservices Setup                ${NC}"
echo -e "${BLUE}==========================================================${NC}"

# Function to copy .env files
setup_envs() {
    echo -e "\n${YELLOW}🔍 Searching for .env.example files...${NC}"
    
    # Find all .env.example files and iterate through them
    find . -name ".env.example" | while read -r example_file; do
        # Determine the target .env filename
        env_file="${example_file%.example}"
        
        if [ ! -f "$env_file" ]; then
            cp "$example_file" "$env_file"
            echo -e "  ${GREEN}✅ Created:${NC} $env_file"
        else
            echo -e "  ${YELLOW}⏩ Skipped:${NC} $env_file (Already exists)"
        fi
    done
}

# Check for Docker installation
check_dependencies() {
    echo -e "\n${YELLOW}🔍 Checking Dependencies...${NC}"
    if ! [ -x "$(command -v docker)" ]; then
        echo -e "  ${RED}❌ Error: Docker is not installed.${NC}" >&2
    else
        echo -e "  ${GREEN}✅ Docker found.${NC}"
    fi

    if ! [ -x "$(command -v docker-compose)" ]; then
        echo -e "  ${YELLOW}⚠️  Warning: docker-compose not found. Using 'docker compose' instead.${NC}"
    else
        echo -e "  ${GREEN}✅ Docker Compose found.${NC}"
    fi
}

# Run functions
check_dependencies
setup_envs


# Add to the SERVICES array if you want to build it with Node
# Or add a specific section for Frontend
echo -e "${BLUE}🎨 Building Frontend: wordium-ui${NC}"
(
    cd frontend/
    npm install
    npm run build --configuration=production
)


# NEW CLOUDINARY WARNING
echo -e "\n${RED}${BOLD}⚠️  ATTENTION: MEDIA UPLOADS${NC}"
echo -e "Your ${YELLOW}.env${NC} files currently contain ${BOLD}FAKE${NC} Cloudinary credentials."
echo -e "1. The project will run fine for testing logic."
echo -e "2. ${RED}Image/Video uploads WILL FAIL${NC} in the UI until you add real credentials."
echo -e "3. Please update Cloudinary keys in: ${YELLOW}backend/posts-service/.env${NC}"


echo -e "\n${BLUE}==========================================================${NC}"
echo -e "${GREEN}✨ Setup Complete!${NC}"
echo -e "${BLUE}==========================================================${NC}"
echo -e "Next steps:"
echo -e "  1. Update secrets in the generated ${YELLOW}.env${NC} files if needed."
echo -e "  2. Run the build script: ${YELLOW}./build.sh${NC} to compile the source code and package the microservices."
echo -e "  3. Run the project: ${YELLOW}docker-compose up --build -d${NC}"
echo -e "  4. Check Eureka Dashboard after 30s: ${YELLOW}http://localhost:8761${NC}"
echo -e "${BLUE}==========================================================${NC}"