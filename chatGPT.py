import speech_recognition as sr
import openai
import os
import sys

# Set OpenAI API key
openai.api_key = sys.argv[1]
text = sys.argv[2]

print("Generating...")
# Generate a response using OpenAI's GPT-3 API
response = openai.Completion.create(
    engine="davinci",
    prompt=format(text),
    max_tokens=1024,
    n=1,
    stop=None,
    temperature=0.5,)
    
# Print the response text
print("Response".format(response.choices[0].text))

with open('output.txt', 'w') as file:
    file.write(response.choices[0].text)

