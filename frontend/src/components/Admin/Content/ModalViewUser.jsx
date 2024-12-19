import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import _ from "lodash";
import { getAccountByUserid } from "../../../Services/apiService";

const ModalViewUser = (props) => {
    const { show, setShow, dataView } = props;

    const handleClose = () => {
        setShow(false);
        setEmail("");
        setPhone("");
        setUsername("");
        setRole("");
        setImage("");
        setPreviewImage("");
        props.resetViewData();
    };

    const [email, setEmail] = useState("");
    const [phone, setPhone] = useState("");
    const [username, setUsername] = useState("");
    const [role, setRole] = useState("");
    const [image, setImage] = useState(null);
    const [previewImage, setPreviewImage] = useState("");

    useEffect(() => {
        if (!_.isEmpty(dataView)) {
            // Cập nhật state khi dataView thay đổi
            setEmail(dataView.email);
            setUsername(dataView.username);
            setImage(dataView.image);
            setPhone(dataView.phone);
            setPreviewImage(`/api/v1/user/image/${dataView.image}`);
            console.log("userid", dataView.userid);
            // Gọi API lấy thông tin account
            fetchAccount(dataView.userid);
        }
    }, [dataView]);  // Thêm dataView vào mảng phụ thuộc

    const fetchAccount = async (userid) => {
        try {
            const response = await getAccountByUserid(userid); // Gọi API lấy danh sách người dùng
            setRole(response.data.data.role); // Set role
            console.log("data", response.data.data.role);
        } catch (error) {
            console.error("Error fetching account:", error);
        }
    };

    return (
        <>
            <Modal show={show} onHide={handleClose} size="lg" backdrop="static"  className='modal-add-user'>
                <Modal.Header closeButton>
                    <Modal.Title>View user</Modal.Title>
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
                        <div className="col-md-4">
                            <label className="form-label">Role</label>
                            <select className="form-select" value={role} onChange={(event) => setRole(event.target.value)}>
                                <option value="USER">USER</option>
                                <option value="ADMIN" >ADMIN</option>
                            </select>
                        </div>
                        <div className="col-md-12 img-preview" >
                            {previewImage ? 
                                <img src={previewImage} alt='ảnh'/> 
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
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalViewUser;
