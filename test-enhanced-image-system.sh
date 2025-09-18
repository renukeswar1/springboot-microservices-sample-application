#!/bin/bash

# Enhanced Image Upload System Test Script
# This script demonstrates the capabilities of the enhanced image processing system

echo "🚀 Enhanced Image Upload System Test"
echo "===================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test URLs
MOVIE_API="http://localhost:8080"
IMAGE_API="http://localhost:8094"

echo -e "\n${BLUE}1. Testing Image Service Health${NC}"
echo "   GET $IMAGE_API/api/images/health"
curl -s $IMAGE_API/api/images/health | jq . || echo "Service not running or JSON parsing failed"

echo -e "\n${BLUE}2. Getting Supported Image Formats${NC}"
echo "   GET $IMAGE_API/api/images/formats"
curl -s $IMAGE_API/api/images/formats | jq . || echo "Service not running or JSON parsing failed"

echo -e "\n${BLUE}3. Testing S3 Key Generation${NC}"
echo "   GET $IMAGE_API/api/images/s3-key?movieId=123&filename=test.jpg&variant=thumbnail"
curl -s "$IMAGE_API/api/images/s3-key?movieId=123&filename=test.jpg&variant=thumbnail" | jq . || echo "Service not running or JSON parsing failed"

# Create a simple test image if it doesn't exist
if [ ! -f "test-image.jpg" ]; then
    echo -e "\n${YELLOW}Creating test image...${NC}"
    # Create a simple colored square using ImageMagick (if available)
    if command -v convert &> /dev/null; then
        convert -size 300x300 xc:blue test-image.jpg
        echo "Created test-image.jpg (300x300 blue square)"
    else
        echo "ImageMagick not available. Please provide a test-image.jpg file to test upload functionality."
    fi
fi

if [ -f "test-image.jpg" ]; then
    echo -e "\n${BLUE}4. Testing Image Validation${NC}"
    echo "   POST $IMAGE_API/api/images/validate (with test-image.jpg)"
    curl -s -X POST \
         -H "Content-Type: multipart/form-data" \
         -F "file=@test-image.jpg" \
         $IMAGE_API/api/images/validate | jq . || echo "Service not running or JSON parsing failed"

    echo -e "\n${BLUE}5. Testing Movie Upload with Image${NC}"
    echo "   POST $MOVIE_API/api/movies (with test-image.jpg)"
    echo "   This will create a movie and trigger async image processing..."
    
    # Attempt movie upload (will fail if movie-api not running, but demonstrates the call)
    UPLOAD_RESULT=$(curl -s -X POST \
         -H "Content-Type: multipart/form-data" \
         -F "title=Test Movie via Enhanced Upload System" \
         -F "file=@test-image.jpg" \
         $MOVIE_API/api/movies 2>/dev/null)
    
    if [ $? -eq 0 ] && [ ! -z "$UPLOAD_RESULT" ]; then
        echo "Upload successful! Response:"
        echo $UPLOAD_RESULT | jq . || echo $UPLOAD_RESULT
        
        # Extract movie ID if available
        MOVIE_ID=$(echo $UPLOAD_RESULT | jq -r '.id // empty' 2>/dev/null)
        if [ ! -z "$MOVIE_ID" ] && [ "$MOVIE_ID" != "null" ]; then
            echo -e "\n${GREEN}✅ Movie created with ID: $MOVIE_ID${NC}"
            echo "The image will be processed asynchronously and uploaded to S3 in multiple variants:"
            echo "  - movies/$MOVIE_ID/original.jpg"
            echo "  - movies/$MOVIE_ID/large.jpg"
            echo "  - movies/$MOVIE_ID/medium.jpg"
            echo "  - movies/$MOVIE_ID/thumbnail.jpg"
        fi
    else
        echo -e "${YELLOW}⚠️  Movie API not running or upload failed${NC}"
        echo "Expected URL: $MOVIE_API/api/movies"
    fi
else
    echo -e "\n${YELLOW}⚠️  No test image available. Skipping upload tests.${NC}"
fi

echo -e "\n${BLUE}6. Enhanced Features Summary${NC}"
echo "============================================="
echo "✅ Multi-variant processing (original, large, medium, thumbnail)"
echo "✅ Asynchronous processing with Kafka"
echo "✅ Metadata extraction (dimensions, EXIF, format)"
echo "✅ Image format validation"
echo "✅ Organized S3 storage structure"
echo "✅ Progressive loading support"
echo "✅ Zoom functionality with high-quality variants"

echo -e "\n${BLUE}7. Service Endpoints${NC}"
echo "==================="
echo "Movie API (Port 8080):"
echo "  POST /api/movies - Upload movie with image"
echo ""
echo "Image Service (Port 8094):"
echo "  GET  /api/images/health - Health check"
echo "  POST /api/images/validate - Validate image"
echo "  GET  /api/images/formats - Supported formats"
echo "  GET  /api/images/s3-key - Generate S3 keys"

echo -e "\n${BLUE}8. Image Variants Generated${NC}"
echo "=========================="
echo "🖼️  Original:  Uploaded as-is (preservation)"
echo "🔍 Large:     1920x1080 max, 95% quality (zoom functionality)"
echo "📱 Medium:    800x600 max, 85% quality (standard display)"
echo "🎯 Thumbnail: 200x200 max, 75% quality (galleries, previews)"

echo -e "\n${BLUE}9. Processing Flow${NC}"
echo "================="
echo "1️⃣  User uploads image → Immediate response"
echo "2️⃣  Kafka event sent → Background processing starts"
echo "3️⃣  Multi-variant generation → 4 optimized sizes"
echo "4️⃣  S3 upload → Organized folder structure"
echo "5️⃣  Database update → Movie record with image URLs"

echo -e "\n${GREEN}🎉 Enhanced Image Upload System Test Complete!${NC}"
echo ""
echo "📖 For detailed documentation, see:"
echo "   - ENHANCED_IMAGE_PROCESSING.md"
echo "   - IMAGE_UPLOAD_SUMMARY.md"
echo ""
echo "🚀 To start the services:"
echo "   cd shared-models && mvn clean install"
echo "   cd movie-api && mvn spring-boot:run &"
echo "   cd imageupload-service && mvn spring-boot:run &"