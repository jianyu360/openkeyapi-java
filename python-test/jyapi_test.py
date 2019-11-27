# -*- coding: utf-8 -*-
import hashlib
import hmac
import base64
import urllib
import urllib.request
import time
import json
import gzip

#签名方法
def hash_hmac(secret,text):
	#在secret后增加&符号
	secret = secret + '&'
    temp = hmac.new(secret.encode('UTF-8'), text.encode('UTF-8'), hashlib.sha1).digest()
    signature = base64.standard_b64encode(temp)      
    return str(signature, 'UTF-8')

requrl = "https://api.jianyu360.com/openkey"
#时间戳
#t = '1535963635'
t = str(int(time.time()))
appid = 'jykw_Otestappid123456'
secret = '9312test'
pagenum = 1
#各参数直接拼接不用再排序了
text = 'action%3Dgetdata%26appid%3D'+appid+'%26pagenum%3D'+pagenum+'%26timestamp%3D'+t

signature = hash_hmac(secret,text)
print(signature)

url_data = {'action':'getdata','appid':appid,'pagenum':pagenum,'timestamp':t,'signature':signature}

url_data_urlencode = urllib.parse.urlencode(url_data).encode('UTF-8')

response = urllib.request.urlopen(url = requrl,data =url_data_urlencode)
res_data = response.read()

try:
	data = gzip.decompress(res_data)
	data = str(data,'UTF-8')
	res = json.loads(data)
	print(res)
except:
	print(json.loads(str(res_data,'UTF-8')))