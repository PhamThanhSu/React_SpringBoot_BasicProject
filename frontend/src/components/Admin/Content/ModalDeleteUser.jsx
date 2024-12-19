import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { deleteUser } from'../../../Services/apiService';
import { toast } from 'react-toastify';

const ModalDeleteUser = (props) => {
    const {show, setShow, dataDelete} = props;

    const handleClose = () => setShow(false);

    const handleSubmidDeleteUser = async () => {
      console.log('dataDelete', dataDelete.userid);
        let data = await deleteUser(dataDelete.userid);
        console.log('data', data);  
        if (data && data.ec === 0) {
            toast.success('Xóa User thành công');
            handleClose(); // Giả sử đây là hàm để đóng modal hoặc reset form
            await props.fetchListUsers(); //Load lại bảng khi tạo thành công user mới
        }
        if (data && data.ec !== 0) {
            toast.error(data.EM);
        }    
    }
    
  return (
    <>
      <Modal show={show} onHide={handleClose} backdrop="static">
        <Modal.Header closeButton>
          <Modal.Title>Confirm Delete the User?</Modal.Title>
        </Modal.Header>
        <Modal.Body> Are you sure tto delete this user. email = 
            <b>
                {dataDelete && dataDelete.email ? dataDelete.email : ""}
            </b>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleSubmidDeleteUser}>
            Confirm
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default ModalDeleteUser;