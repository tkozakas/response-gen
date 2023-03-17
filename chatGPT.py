import speech_recognition as sr
import openai
import os
import sys

from dotenv import load_dotenv
load_dotenv()

# Set OpenAI API key
openai.api_key = os.getenv("OPENAI_API_KEY")

text = sys.argv[1]

# Generate a response using OpenAI's GPT-3 API
response = openai.ChatCompletion.create(
model='gpt-3.5-turbo',
  messages=[
    {"role": "user", "content": text}],
max_tokens=100,
temperature=2,
)

# Removing newline character from string
# using loop
res = []
for sub in response["choices"][0]["message"]["content"]:
    res.append(sub.replace("\n", ""))

# Print the response text
print("".join(res))

