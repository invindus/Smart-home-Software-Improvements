import json
import requests

def test_code1():
    response=requests.get("http://localhost:8080")
    json_response=response.json()
    assert(json_response["code"]==404)


def test_message1():
    response=requests.get("http://localhost:8080")
    json_response=response.json()
    assert(json_response["message"]=="HTTP 404 Not Found")


def test_code2():
    response=requests.get("http://localhost:8080/smarthome")
    json_response=response.json()
    assert(json_response["code"]==404)


def test_message2():
    response=requests.get("http://localhost:8080/smarthome")
    json_response=response.json()
    assert(json_response["message"]=="HTTP 404 Not Found")

def test_code3():
    response=requests.get("http://localhost:8080/smarthome/state")
    json_response=response.json()
    assert(json_response["code"]==404)


def test_message3():
    response=requests.get("http://localhost:8080/smarthome/state")
    json_response=response.json()
    assert(json_response["message"]=="HTTP 404 Not Found")

def test_code4():
    response=requests.get("http://localhost:8080/smarthome/state/mse")
    code = response.status_code
    assert(code == 401)

def test_message4():
    response=requests.get("http://localhost:8080/smarthome/state/mse", auth=('admin', '1234'))
    body = response.content
    assert(b'Tartan House Control Panel' in body)
    assert(response.status_code == 200)

def test_update():
    update = { "door": "open",
               "alarmArmed": "armed"
            }
    post = requests.post("http://localhost:8080/smarthome/update/mse", auth=('admin', '1234'), json=update)
    assert(post.status_code == 200)

    response=requests.get("http://localhost:8080/smarthome/state/mse", auth=('admin', '1234'))
    assert(b'Door open' in response.content)
    assert(b'Alarm Active' in response.content)

    update = { "door": "closed" }
    post = requests.post("http://localhost:8080/smarthome/update/mse", auth=('admin', '1234'), json=update)
    assert(post.status_code == 200)

    response=requests.get("http://localhost:8080/smarthome/state/mse", auth=('admin', '1234'))
    assert(b'Door closed' in response.content)

def test_bad_update():
    update = {"notARealKey": "405"}
    response=requests.post("http://localhost:8080/smarthome/update/mse", auth=('admin', '1234'), json=update)
    assert(response.status_code == 400)

