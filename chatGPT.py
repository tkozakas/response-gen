import speech_recognition as sr
import openai
import os
import sys

# Set OpenAI API key
openai.api_key = "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"
text = sys.argv[1]

# Print input text
print("Input: "+ text)

print("Generating...")
# Generate a response using OpenAI's GPT-3 API
response = openai.Completion.create(
    engine="davinci",
    prompt=format(text),
    max_tokens=100,
    n=1,
    stop=None,
    temperature=0.1,)
    
# Print the response text
print("Response: "+ response.choices[0].text)

