import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';

export default function RegisterPage() {

    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('');
    const [error, setError] = useState('');

    const navigate = useNavigate();

    const handleSumit = async (e) => {
        e.preventDefault();
        setError(null);

        try {
            const response = await fetch('http://localhost:9090/api/user/register', {
                method: 'POST',
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({username, password, email, role}),
            });

            const data = await response.json();

            if (response.ok) {
                console.log('User registration successful', data)
                navigate("/")
            } else {
                throw new Error(data.Error || 'Registration failed')
            }
        } catch (error) {
            setError(error.message);
        }
    }

    return (
        <div className='register-main'>
            <div className='register-card'>
                <h1>REGISTER</h1>
                {error && <p className='error-message'>{error}</p>}
                <form onSubmit={handleSumit}>
                    <input type="text" placeholder='Enter new username' value={username} onChange={(e) => { setUsername(e.target.value) }} required />
                    <input type="text" placeholder='Enter your email' value={email} onChange={(e) => { setEmail(e.target.value) }} required />
                    <input type="text" placeholder='Enter new password' value={password} onChange={(e) => { setPassword(e.target.value) }} required />
                    <select value={role} onChange={(e) => { setRole(e.target.value) }} required>
                        <option value="" disabled>Select your role</option>
                        <option value="ADMIN">ADMIN</option>
                        <option value="SEEKER">JOBSEEKER</option>
                    </select>
                    <button type='submit'>Sign In</button>
                </form>
                <a href="/">Already a User?Login</a>
            </div>
        </div>
    )
}
