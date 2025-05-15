import React, { useEffect, useState } from 'react'
import './assets/Style.css'
import {Link, useNavigate} from 'react-router-dom'

export default function JobList({title}) {
    const [jobs, setJobs] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {

        fetchJobs(title);

    }, [title]);

    const fetchJobs = async (title) => {
        try {
            console.log("fetching...")
            const response = await fetch(`http://localhost:9090/api/post/search${title ? `?title=${title}`: ""}`, {
                credentials: 'include'
            })

            const data = await response.json();

            if (data) {
                setJobs(data)
            } else {
                setJobs([]);
            }

        } catch (error) {
            console.error("error in fetching ", error);
            setJobs([]);
        }



    };
    
    return (
        <div className='job-main'>
            {jobs.length!=0?  <div className='jobcard-grid'>
                {jobs.map((job) =>(
                    <div key={job.id} className={`job-card ${job.status === 'Closed' ? 'job-disabled' : ''}`}>
                        <h3>{job.title}</h3>
                        <p> {job.description}</p>
                        <h5>Experience: {job.experienceRequired}</h5>
                        {job.status == 'Closed' ? (<div><Link>Closed</Link></div>): (<div><Link to="/apply" state={{job}}>← Apply Here</Link></div>) }
                        
                    </div>
                ))}
                
            </div>:
            <div className='no-job-message'>
                <p>No Jobs Available....</p>
            </div>
            }
        </div>
    )
}
