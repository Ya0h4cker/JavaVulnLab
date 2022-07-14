from email.mime import base
import sys
import base64
import uuid
from random import Random
from Crypto.Cipher import AES

def get_file_data(filename):
    with open(filename, 'rb') as f:
        data = f.read()
    return data

def aes_enc(data):
    BS = AES.block_size
    pad = lambda s: s + ((BS - len(s) % BS) * chr(BS - len(s) % BS)).encode()
    key = "kPH+bIxk5D2deZiIxcaaaA=="
    mode = AES.MODE_CBC
    iv = uuid.uuid4().bytes
    encryptor = AES.new(base64.b64decode(key), mode, iv)
    ciphertext = base64.b64encode(iv + encryptor.encrypt(pad(data)))
    return ciphertext

if __name__ == '__main__':
    data = get_file_data('../serialized.bin')
    print(aes_enc(data))
