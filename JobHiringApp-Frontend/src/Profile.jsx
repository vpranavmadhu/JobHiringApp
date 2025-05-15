import React, { useEffect, useState } from 'react';
import Header from './Header';
import Footer from './Footer';
import { useNavigate } from 'react-router-dom';
import './assets/Profile.css'; // <-- Import the CSS file

export default function Profile() {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    getUserDetails();
  }, []);

  const getUserDetails = async () => {
    try {
      const response = await fetch('http://localhost:9090/api/user/getUser', {
        credentials: 'include',
      });

      const data = await response.json();
      if (data) {
        setUser(data);
      } else {
        console.error('Failed to fetch user data:', data);
      }
    } catch (error) {
      console.error('Error fetching user data:', error);
    }
  };

  // const handleEdit = () => {
  //   navigate('/edit-profile');
  // };

  const handleDeleteAccount = async () => {
    if (!window.confirm("Are you sure you want to delete your account? This action is irreversible.")) return;

    try {
      const response = await fetch('http://localhost:9090/api/user/deleteuser', {
        method: 'DELETE',
        credentials: 'include',
      });
      const data = await response.text();
      if (response.ok) {
        alert(data);
        navigate('/');
      } else {
        alert('Failed to delete account');
      }
    } catch (error) {
      console.error('Error deleting account:', error);
    }
  };

  return (
    <div className="home-main">
      <div className="home-header">
        <Header showSearch={false} role={"SEEKER"} />
      </div>

      <div className="home-content">
        {user ? (
          <div className="profile-card">
            <h2 className="profile-title">User Profile</h2>
            <div className="profile-body">
              <div className="profile-avatar">
                {user.username && <span>{user.username.charAt(0).toUpperCase()}</span>}
              </div>
              <div className="profile-info">
                <p><strong>Username:</strong> {user.username}</p>
                <p><strong>Email:</strong> {user.email}</p>
                <p><strong>Role:</strong> {user.role}</p>
              </div>
            </div>
            <div className="profile-buttons">
              {/* <button className="profile-edit-btn" onClick={handleEdit}>Edit</button> */}
              <button className="profile-delete-btn" onClick={handleDeleteAccount}>Delete Account</button>
            </div>
          </div>

        ) : (
          <p>Loading user data...</p>
        )}
      </div>

      <div className="home-footer">
        <Footer />
      </div>
    </div>
  );
}
