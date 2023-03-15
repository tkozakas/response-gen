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
response = openai.ChatCompletion.create(
model='gpt-3.5-turbo',
  messages=[
    {"role": "user", "content": text}],
max_tokens=50,
temperature=0,
)

# Removing newline character from string
# using loop
res = []
for sub in response["choices"][0]["message"]["content"]:
    res.append(sub.replace("\n", ""))
    
# Print the response text
print(response["choices"][0]["message"]["content"])

