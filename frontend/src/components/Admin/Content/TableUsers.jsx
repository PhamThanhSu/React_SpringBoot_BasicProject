import React from "react";
import './ManageUser.scss';

const TableUsers = (props) => {
    const { listUsers, handleClickBtnView, handleClickBtnUpdate, handleClickBtnDelete } = props;

    return (
        <>
            <table className="table table-hover table-bordered table-striped">
                <thead>
                    <tr>
                        <th scope="col">No</th>
                        <th scope="col">Username</th>
                        <th scope="col">Email</th>
                        <th scope="col">Image</th>
                        <th scope="col">Action</th>
                    </tr>
                </thead>
                <tbody>
                    {listUsers && listUsers.length > 0 ? (
                        listUsers.map((item, index) => (
                            <tr key={`table-users-${index}`}>
                                <td>{index + 1}</td>
                                <td>{item.username}</td>
                                <td>{item.email}</td>
                                <td>
                                    {item.image ? (
                                        <img 
                                            src={`/api/v1/user/image/${item.image}`} 
                                            alt={`User ${item.username}`} 
                                            style={{ width: '100%', height: '50px', objectFit: 'cover' }} 
                                        />
                                    ) : (
                                        <span>No Image</span> // Nếu không có ảnh, hiển thị thông báo
                                    )}
                                </td>
                                <td>
                                    <button className="btn btn-secondary" onClick={() => handleClickBtnView(item)}>View</button>
                                    <button className="btn btn-warning mx-3" onClick={() => handleClickBtnUpdate(item)}>Update</button>
                                    <button className="btn btn-danger" onClick={() => handleClickBtnDelete(item)}>Delete</button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan={'6'}>No data found</td>
                        </tr>
                    )}
                </tbody>
            </table> 
        </>
    );
};

export default TableUsers;
