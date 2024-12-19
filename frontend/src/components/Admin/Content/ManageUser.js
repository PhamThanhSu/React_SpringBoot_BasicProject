import ModalCreateUser from "./ModalCreateUser";
import './ManageUser.scss';
import { FiUserPlus } from "react-icons/fi";
import TableUsers from "./TableUsers";
import ModalUpdateUser from "./ModalUpdateUser";
import { useEffect, useState } from "react";
import { getAllUsers } from "../../../Services/apiService";
import ModalViewUser from "./ModalViewUser";
import ModalDeleteUser from "./ModalDeleteUser";

const ManageUser = (props) => {
    const [showModalCreateUser, setShowModalCreateUser] = useState(false);
    const [showModalUpdateUser, setShowModalUpdateUser] = useState(false);
    const [showModalViewUser, setShowModalViewUser] = useState(false);
    const [showModalDeleteUser, setShowModalDeleteUser] = useState(false);
    const [listUsers, setListUsers] = useState([]); // State để refresh bảng
    const [dataUpdate, setDataUpdate] = useState({});
    const [dataView, setDataView] = useState({});
    const [dataDelete, setDataDelete] = useState({});
    const [loading, setLoading] = useState(true); // Trạng thái loading

    useEffect(() => {
        fetchListUsers(); // Gọi hàm fetchListUsers khi component render
    }, []);

    // Hàm để gọi API lấy danh sách người dùng
    const fetchListUsers = async () => {
        try {
            const response = await getAllUsers(); // Gọi API lấy danh sách người dùng
            console.log(response.data); // Kiểm tra dữ liệu nhận được
            setListUsers(response.data);
            setLoading(false); // Đánh dấu kết thúc việc tải dữ liệu
        } catch (error) {
            console.error("Error fetching user:", error);
            setLoading(false);
        }
    };
    if (loading) {
        return <div>Loading...</div>; // Hiển thị loading khi dữ liệu đang được tải
    }
    
    const handleClickBtnUpdate = (user) => {
        setShowModalUpdateUser(true);
        setDataUpdate(user);
        
    };

    const handleClickBtnView = (user) => {
        setShowModalViewUser(true);
        setDataView(user);
        console.log('user', dataView); // Có thể gặp vấn đề do state cập nhật không đồng bộ
    };


    const handleClickBtnDelete = (user) => {
        setShowModalDeleteUser(true);
        setDataDelete(user);
    };

    const resetUpdateData = () => {
        setDataUpdate({});
    }

    const resetViewData = () => {
        setDataView({});
    }

    return (
        <div className="manage-user-container">
            <div className="title">
                ManageUser
            </div>
            <div className="users-content">
                <div className="btn-add-new">
                    <button className="btn btn-primary" onClick={() => setShowModalCreateUser(true)}>
                        <FiUserPlus /> Add new users
                    </button>
                </div>
                <div className="table-users-container">
                    <TableUsers handleClickBtnUpdate={handleClickBtnUpdate} handleClickBtnView={handleClickBtnView} handleClickBtnDelete={handleClickBtnDelete} listUsers={listUsers} />
                </div>
                <ModalCreateUser show={showModalCreateUser} setShow={setShowModalCreateUser} fetchListUsers={fetchListUsers} />
                <ModalUpdateUser show={showModalUpdateUser} setShow={setShowModalUpdateUser} fetchListUsers={fetchListUsers} dataUpdate={dataUpdate} resetUpdateData={resetUpdateData} />
                <ModalViewUser show={showModalViewUser} setShow={setShowModalViewUser} dataView={dataView} resetViewData={resetViewData} />
                <ModalDeleteUser show={showModalDeleteUser} setShow={setShowModalDeleteUser} dataDelete={dataDelete} resetUpdateData={resetUpdateData} fetchListUsers={fetchListUsers} />
            </div>
        </div>
    );
}

export default ManageUser;
