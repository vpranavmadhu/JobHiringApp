import React, { useEffect, useRef, useState } from 'react';
import Header from './Header';
import Footer from './Footer';
import Modal from './Modal';
import './assets/Modal.css'; // Modal styling

export default function VeiwJobsAdmin() {
  const [jobs, setJobs] = useState([]);
  const fetchedOnce = useRef(false);

  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [jobToDelete, setJobToDelete] = useState(null);

  const [showStatusModal, setShowStatusModal] = useState(false);
  const [jobToUpdateStatus, setJobToUpdateStatus] = useState(null);
  const [newStatus, setNewStatus] = useState('');

  const [showApplicantsModal, setShowApplicantsModal] = useState(false);
  const [applicants, setApplicants] = useState([]);
  const [loadingApplicants, setLoadingApplicants] = useState(false);

  useEffect(() => {
    if (!fetchedOnce.current) {
      fetchJobs();
      fetchedOnce.current = true;
    }
  }, []);

  const fetchJobs = async () => {
    try {
      const response = await fetch('http://localhost:9090/admin/getjobs', {
        credentials: 'include'
      });
      const data = await response.json();
      setJobs(data || []);
    } catch (error) {
      console.error("Error fetching jobs:", error);
      setJobs([]);
    }
  };

  const handleDelete = (jobId) => {
    setJobToDelete(jobId);
    setShowDeleteModal(true);
  };

  const confirmDelete = async () => {
    try {
      const response = await fetch(`http://localhost:9090/admin/deletejob?jobId=${jobToDelete}`, {
        method: 'DELETE',
        credentials: 'include'
      });
      if (response.ok) {
        alert("Job deleted successfully");
        fetchJobs();
      } else {
        alert("Failed to delete job");
      }
    } catch (error) {
      console.error("Error deleting job:", error);
    } finally {
      setShowDeleteModal(false);
      setJobToDelete(null);
    }
  };

  const handleChangeStatus = (jobId) => {
    setJobToUpdateStatus(jobId);
    setShowStatusModal(true);
  };

  const confirmStatusChange = async () => {
    try {

      console.log(jobToUpdateStatus)
      console.log(newStatus)
      const response = await fetch(`http://localhost:9090/admin/updatestatus`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ jobId: jobToUpdateStatus, status: newStatus }),
      });
      if (response.ok) {
        alert("Status updated");
        fetchJobs();
      } else {
        alert("Failed to update status");
      }
    } catch (error) {
      console.error("Error updating status:", error);
    } finally {
      setShowStatusModal(false);
      setNewStatus('');
      setJobToUpdateStatus(null);
    }
  };

  const handleViewApplicants = async (jobId) => {
    setLoadingApplicants(true);
    try {
      const response = await fetch(`http://localhost:9090/admin/viewapplicants?jobId=${jobId}`, {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        console.log("appli: ", data)
        setApplicants(data);
        setShowApplicantsModal(true);
      } else {
        alert("No applicants found");
      }
    } catch (error) {
      console.error("Error fetching applicants:", error);
    } finally {
      setLoadingApplicants(false);
    }
  };

  return (
    <div className='home-main'>
      <div className='home-header'>
        <Header showSearch={false} role={"ADMIN"} />
      </div>

      <div className='adminjobhome-content'>
        <div className="adminjobcard-cards-container">
          {jobs.map((job) => (
            <div key={job.id} className="adminjobcard-card">
              <h3>{job.title}</h3>
              <p><strong>Description:</strong> {job.description}</p>
              <p><strong>Skills Required:</strong> {job.skillsRequired.join(', ')}</p>
              <p><strong>Experience Required:</strong> {job.experienceRequired}</p>
              <p><strong>Location:</strong> {job.location}</p>
              <p><strong>Salary:</strong> {job.salary}</p>
              <p><strong>Status:</strong> {job.status}</p>
              <p><strong>Job Type:</strong> {job.jobType}</p>
              <p><strong>Posted On:</strong> {new Date(job.postedOn).toLocaleDateString()}</p>

              <div className="adminjobcard-card-buttons">
                <button onClick={() => handleDelete(job.id)}>Delete</button>
                <button onClick={() => handleChangeStatus(job.id)}>Change Status</button>
                <button onClick={() => handleViewApplicants(job.id)}>View Applicants</button>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className='home-footer'>
        <Footer />
      </div>

      {/* Delete Modal */}
      {showDeleteModal && (
        <Modal title="Confirm Delete" onClose={() => setShowDeleteModal(false)}>
          <p>Are you sure you want to delete this job?</p>
          <button className='btn-1' onClick={confirmDelete}>Yes, Delete</button>
          <button className='btn-2' onClick={() => setShowDeleteModal(false)}>Cancel</button>
        </Modal>
      )}

      {/* Status Change Modal */}
      {showStatusModal && (
  <Modal title="Change Job Status" onClose={() => setShowStatusModal(false)}>
    <p>Select new status for the job:</p>
    <div>
      <label>
        <input
          type="radio"
          name="jobStatus"
          value="Open"
          checked={newStatus === 'Open'}
          onChange={(e) => setNewStatus(e.target.value)}
        />
        Open
      </label>
      <br />
      <label>
        <input
          type="radio"
          name="jobStatus"
          value="Closed"
          checked={newStatus === 'Closed'}
          onChange={(e) => setNewStatus(e.target.value)}
        />
        Close
      </label>
    </div>
    <br />
    <button className='btn-1' onClick={confirmStatusChange} disabled={!newStatus}>Update Status</button>
    <button className='btn-2' onClick={() => setShowStatusModal(false)}>Cancel</button>
  </Modal>
)}

      {/* Applicants Modal */}
      {showApplicantsModal && (
        <Modal title="Applicants" onClose={() => setShowApplicantsModal(false)}>
          {loadingApplicants ? (
            <p>Loading...</p>
          ) : applicants.length > 0 ? (
            <ul>
              {applicants.map((app, idx) => (
                <li key={idx}>
                  {typeof app === 'object' ? (
                    <>
                      <p><strong>Name:</strong> {app.username}</p>
                      <p><strong>Email:</strong> {app.email}</p>
                    </>
                  ) : (
                    <p>User ID: {app}</p>
                  )}
                  <hr />
                </li>
              ))}
            </ul>
          ) : (
            <p>No applicants found.</p>
          )}
        </Modal>
      )}
    </div>
  );
}
