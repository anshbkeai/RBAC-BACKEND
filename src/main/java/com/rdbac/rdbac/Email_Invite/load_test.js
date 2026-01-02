import http from 'k6/http';
import { check, group } from 'k6';

export const options = {
  vus: 5,
  duration: '3s',
};

const USERNAME = 'vedansh.shri09@gmail.com';
const PASSWORD = 'vedansh.shri09@123';
const BASE_URL = 'http://localhost:8080';

export function setup() {
  const loginRes = http.post(
    `${BASE_URL}/auth/login`,
    JSON.stringify({ email: USERNAME, password: PASSWORD }),
    { headers: { 'Content-Type': 'application/json' } }
  );

  const parsed = (loginRes.body);
  const authToken = parsed;

  check(authToken, { 'Logged in successfully': () => authToken && authToken.length > 0 });

  return authToken;
}

export default function (authToken) {
   // authToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2ZWRhbnNoLnNocmkwOUBnbWFpbC5jb20iLCJqdGkiOiJjY2I1NmYxMy00OTIwLTRkNmQtYmZlZS1hY2NmZjM3YmI0ZWMiLCJpYXQiOjE3NTQzMDkxOTAsImV4cCI6MTc1NDMwOTc5MH0.8cCcrCI_RKWUMaDoJ425a7uGds0dhYlpG186OSOKtPo";
  authToken= "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2ZWRhbnNoLnNocmkwOUBnbWFpbC5jb20iLCJqdGkiOiIyMzRiMjk0ZC1kYWMwLTQ0NTYtOWU2Yy02NWQwMzU1OGU5MTYiLCJpYXQiOjE3NTQzMTA1MDIsImV4cCI6MTc1NDMxMTEwMn0.ke7jK5ykuVh1yoPgw9OC7B99XlyglI6FS6zi8NUteHI";
   const headers = {
    Authorization: `Bearer ${authToken}`,
    'Content-Type': 'application/json',
  };

  const URL = `${BASE_URL}/email/invite`;

  group('01. Send invite to user', () => {
    const payload = {
      invited_user_email: "ansh.badam@gmail.com",
      organisation_id: "e87f352b-710d-49cb-8898-6c3937b25b7a",
    };

    const res = http.post(URL, JSON.stringify(payload), { headers });

    check(res, {
      'Invite sent successfully': (r) => r.status === 200,
    });

    if (res.status !== 200) {
      console.log(`Invite failed: ${res.status} ${res.body}`);
    }
  });
}
