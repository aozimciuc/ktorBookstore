import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
stages : [
    { duration: '10s', target: 10 },
    { duration: '20s', target: 10 },
    { duration: '10s', target: 0 },
  ]
};

export default function() {
  let res = http.get('http://172.21.192.1:8080/');
  check(res, {'status is 200': (r) => r.status === 200});
  sleep(1);
}