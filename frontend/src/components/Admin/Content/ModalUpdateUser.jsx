import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { FiUpload } from "react-icons/fi";
import { ToastContainer, toast } from 'react-toastify';
import { getAccountByUserid } from "../../../Services/apiService";
import { putUpdateUser } from '../../../Services/apiService';
import _, { update } from 'lodash';

const ModalUpdateUser = (props) => {
    const { show, setShow, dataUpdate } = props;

    const handleClose = () => {
        setShow(false);
        setEmail("");
        setPhone("");
        setPassword("");
        setUsername("");
        setRole("USER");
        setuserImage("");
        setPreviewImage("");
        props.resetUpdateData();
    };

    const [email, setEmail] = useState("");
    const [phone, setPhone] = useState("");
    const [password, setPassword] = useState("");
    const [username, setUsername] = useState("");
    const [role, setRole] = useState("USER");
    const [userImage, setuserImage] = useState(null);
    const [previewImage, setPreviewImage] = useState("");

    useEffect(() => {
        if(!_.isEmpty(dataUpdate)){
            //update state
            setEmail(dataUpdate.email);
            setPhone(dataUpdate.phone);
            setUsername(dataUpdate.username);
            setuserImage(dataUpdate.image);
            setPreviewImage(`/api/v1/user/image/${dataUpdate.image}`);
            // Gọi API lấy thông tin account
            fetchAccount(dataUpdate.userid);
        }
    }, [dataUpdate]);

    const fetchAccount = async (userid) => {
        try {
            const response = await getAccountByUserid(userid); // Gọi API lấy danh sách người dùng
            setRole(response.data.data.role); // Set role
            console.log("data", response.data.data.role);
        } catch (error) {
            console.error("Error fetching account:", error);
        }
    };

    const handleUploadImage = (event) => {
        if(event.target && event.target.files && event.target.files[0]){
            setPreviewImage(URL.createObjectURL(event.target.files[0]));
            setuserImage(event.target.files[0]);
        }
    }

    const validateEmail = (email) => {
        return String(email)
          .toLowerCase()
          .match(
            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
          );
    };
    const handleSubmitUpdateUser = async(userid) => {
        console.log('userid update', userid);
        //validate
        const isValidatEmail = validateEmail(email);
        if(!isValidatEmail){
            toast.error('Invalid email')
            return;
        }
        try {
            // Gọi hàm postCreateNewUser để tạo user mới
            let data = await putUpdateUser(userid, email, phone, password, username, role, userImage, null);
            console.log('data', data);

            if (data && data.ec === 0) { 
                toast.success('Cập nhật User thành công');
                handleClose();
                await props.fetchListUsers(); // Load lại bảng khi tạo thành công user mới
            } else if (data.ec === 1) {  
                toast.error(data.message);
            }

        } catch (error) {
            // Xử lý khi có lỗi từ API
            console.error(error);
            toast.error('Có lỗi xảy ra, vui lòng thử lại');
        }
    }

    return (
        <>
            {/* <Button variant="primary" onClick={handleShow}>
                Launch demo modal
            </Button> */}

            <Modal show={show} onHide={handleClose} size="lg" backdrop="static"  className='modal-add-user'>
                <Modal.Header closeButton>
                    <Modal.Title>Update user</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form className="row g-3">
                        <div className="col-md-6">
                            <label className="form-label">Email</label>
                            <input type="email" className="form-control" value={email} onChange={(event) => setEmail(event.target.value)}/>
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Phone</label>
                            <input type="phone" className="form-control" value={phone} onChange={(event) => setPhone(event.target.value)}/>
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Username</label>
                            <input type="text" className="form-control" value={username} onChange={(event) => setUsername(event.target.value)}/>
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Password</label>
                            <input type="password" className="form-control" value={password} onChange={(event) => setPassword(event.target.value)}/>
                        </div>
                        <div className="col-md-12 up-load">
                            <label className="form-label label-upload" htmlFor='labelUpload'>
                                <FiUpload />Upload File Image
                            </label>
                            <input type="file" id="labelUpload" hidden onChange={(event) => handleUploadImage(event)}/>
                        </div>
                        <div className="col-md-4 role-combobox">
                            <label className="form-label label-role">Role</label>
                            <select className="form-select role-select" value={role} onChange={(event) => setRole(event.target.value)}>
                                <option value="USER">USER</option>
                                <option value="ADMIN" >ADMIN</option>
                            </select>
                        </div>
                        <div className="col-md-12 img-preview" >
                            {previewImage ? 
                                <img src={previewImage} /> 
                                : 
                                <span>Preview Image</span>
                            }
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={() => handleSubmitUpdateUser(dataUpdate.userid)}>
                        Save Changes
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalUpdateUser;