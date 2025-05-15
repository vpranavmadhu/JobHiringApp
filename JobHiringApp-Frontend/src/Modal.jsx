import React from 'react';
import './assets/Modal.css';

export default function Modal({ title, children, onClose }) {
  return (
    <div className="modal-overlay">
      <div className="modal-box">
        <h3>{title}</h3>
        <div className="modal-content">{children}</div>
        <button className="modal-close" onClick={onClose}>&times;</button>
      </div>
    </div>
  );
}
