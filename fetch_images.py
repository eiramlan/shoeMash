import os
import requests

# Replace this with your actual Pexels API key
API_KEY = 'ZqprZv8o9uIGgOQ9fDpHahlaI0mVmWqkmCz0MV9pNd5MYraXIiPrPGxP'

# Set the Authorization header with just the API key (no Bearer prefix)
headers = {
    'Authorization': API_KEY,  # No 'Bearer' prefix
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
    'Accept-Language': 'en-US,en;q=0.9'
}

# Define the search query
url = "https://api.pexels.com/v1/search?query=nike+shoes&per_page=100"

def download_image(image_url, image_name, folder_path):
    try:
        # Send the same headers as the API request to download the image
        response = requests.get(image_url, headers=headers, stream=True)
        if response.status_code == 200:
            # Create the folder if it doesn't exist
            if not os.path.exists(folder_path):
                os.makedirs(folder_path)

            # Save the image to the folder
            image_path = os.path.join(folder_path, f"{image_name}.jpg")
            with open(image_path, 'wb') as f:
                for chunk in response.iter_content(1024):
                    f.write(chunk)
            print(f"Downloaded: {image_path}")
        else:
            print(f"Failed to download image {image_name}. HTTP Status: {response.status_code}")
    except Exception as e:
        print(f"Error downloading {image_name}: {e}")

def get_shoe_images():
    # Send GET request to Pexels API with the headers
    response = requests.get(url, headers=headers)

    # Check the response status
    if response.status_code == 200:
        # Parse the JSON response
        data = response.json()

        # Folder to save images
        folder_path = 'shoe_images'

        # Iterate over each photo and download the medium-sized image
        for i, photo in enumerate(data['photos']):
            image_url = photo['src']['medium']  # Use 'medium' size, can be changed
            image_name = f'shoe_image_{i+1}'  # Name the image files
            download_image(image_url, image_name, folder_path)
    else:
        print(f"Failed to retrieve images. Status code: {response.status_code}")
        print(response.text)  # Output the full error message if needed

# Run the function to fetch and download shoe images
get_shoe_images()
