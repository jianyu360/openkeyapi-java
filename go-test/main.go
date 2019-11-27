package main

import (
	"bytes"
	"crypto/hmac"
	"crypto/sha1"
	"encoding/base64"
	"fmt"
	"hash"
	"io"
	"log"
	"net/url"
	"os"
	"sort"
	"strings"
)

//签名使用方法
//main action params secret
//main getdata appid=testappid123&pagenum=1&timestamp=1535963635 secret123
func main() {
	if len(os.Args) > 1 && len(os.Args) < 4 {
		log.Println("参数错误", len(os.Args), os.Args[0])
	} else if len(os.Args) > 3 {
		p := strings.Split(os.Args[2], "&")
		log.Println(p)
		param := [][]string{}
		for _, v := range p {
			param = append(param, strings.Split(v, "="))
		}
		log.Println(Signature(os.Args[1], param, os.Args[3]))
	}
}

func Signature(action string, param [][]string, secret string) (signedStr string) {
	ps := &paramSorter{[]string{}, []string{}}
	ps.Keys = append(ps.Keys, "action")
	ps.Vals = append(ps.Vals, action)
	if len(param) > 0 {
		for _, v := range param {
			ps.Keys = append(ps.Keys, v[0])
			ps.Vals = append(ps.Vals, v[1])
		}
	}
	ps.Sort()
	reqStr := ps.String()
	str := percentEncode(reqStr)
	str = SP(str, "%3A", "%253A", -1)
	h := hmac.New(func() hash.Hash { return sha1.New() }, []byte(secret+"&"))
	io.WriteString(h, str)
	signedStr = base64.StdEncoding.EncodeToString(h.Sum(nil))
	return
}

var SP = strings.Replace

func percentEncode(str string) string {
	str = url.QueryEscape(str)
	str = SP(SP(SP(str, "+", "%20", -1), "*", "%2A", -1), "%7E", "~", -1)
	return str
}

type paramSorter struct {
	Keys []string
	Vals []string
}

func (ps *paramSorter) String() string {
	str := ""
	for n, k := range ps.Keys {
		str += k + "=" + ps.Vals[n]
		if n < len(ps.Keys)-1 {
			str += "&"
		}
	}
	return str
}
func (ps *paramSorter) Query() string {
	str := ""
	for n, k := range ps.Keys {
		str += k + "=" + url.QueryEscape(ps.Vals[n])
		if n < len(ps.Keys)-1 {
			str += "&"
		}
	}
	return str
}

func (ps *paramSorter) Sort() {
	sort.Sort(ps)
}
func (ps *paramSorter) Len() int {
	return len(ps.Vals)
}

func (ps *paramSorter) Less(i, j int) bool {
	return bytes.Compare([]byte(ps.Keys[i]), []byte(ps.Keys[j])) < 0
}
func (ps *paramSorter) Swap(i, j int) {
	ps.Vals[i], ps.Vals[j] = ps.Vals[j], ps.Vals[i]
	ps.Keys[i], ps.Keys[j] = ps.Keys[j], ps.Keys[i]
}
