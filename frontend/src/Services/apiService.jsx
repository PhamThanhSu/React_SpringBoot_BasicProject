//import axios from "../ultils/axiosCustomize";
import axios from "axios";

const postCreateNewUser = async (email, phone, password, username, role, userImage, birthday) => {
    const data = new FormData();
    
    // Kiểm tra nếu image không phải null hoặc undefined thì mới thêm vào FormData
    if (userImage) {
        data.append('userImage', userImage);
    }
    
    data.append('email', email);
    data.append('phone', phone);
    data.append('password', password);
    data.append('username', username);
    data.append('role', role);
    
    // Kiểm tra xem birthday có được cung cấp không, nếu có thì thêm vào FormData
    if (birthday) {
        data.append('birthday', birthday); // Ví dụ: '1990-12-25'
    }

    try {
        const response = await axios.post('/api/v1/create-user', data, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            validateStatus: (status) => {
                // Chỉ coi là lỗi khi mã trạng thái >= 500
                return status >= 200 && status < 500;
            }
        });
        return response.data;

    } catch (error) {
        // Nếu có lỗi hệ thống hoặc kết nối, xử lý ở đây
        console.error('Lỗi khi tạo người dùng:', error);
        throw error;  
    }
};

const putUpdateUser = async (userid, email, phone, password, username, role, userImage, birthday) => {
    const data = new FormData();
    
    // Kiểm tra nếu image không phải null hoặc undefined thì mới thêm vào FormData
    if (userImage) {
        
    }
    data.append('userImage', userImage);
    data.append('email', email);
    data.append('phone', phone);
    data.append('password', password);
    data.append('username', username);
    data.append('role', role);
    
    // Kiểm tra xem birthday có được cung cấp không, nếu có thì thêm vào FormData
    if (birthday) {
        data.append('birthday', birthday); // Ví dụ: '1990-12-25'
    }

    try {
        const response = await axios.put(`/api/v1/update-user/${userid}`, data, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            validateStatus: (status) => {
                // Chỉ coi là lỗi khi mã trạng thái >= 500
                return status >= 200 && status < 500;
            }
        });
        return response.data;

    } catch (error) {
        // Nếu có lỗi hệ thống hoặc kết nối, xử lý ở đây
        console.error('Lỗi khi cập nhật người dùng:', error);
        throw error;  
    }
};

const getAllUsers = async () => {
    // Đã gắn proxy ở package.json, nên không cần ghi đầy đủ http://localhost:8080/api/v1/user
    return await axios.get('/api/v1/user'); 
};
// Gọi api lấy thông tin account theo userid
const getAccountByUserid = async (userid) => {
    return await axios.get(`/api/v1/account/${userid}`);
}

const deleteUser = async (userid) => {
    console.log('userid', userid);
    try {
        const response = await axios.delete(`/api/v1/delete-user/${userid}`,{
            validateStatus: (status) => {
                // Trả về true cho mọi mã trạng thái từ 200 đến 500
                return status >= 200 && status < 500;
            }
        });
        return response.data;
    } catch (error) {
        // Nếu có lỗi hệ thống hoặc kết nối, xử lý ở đây
        console.error('Lỗi khi xóa người dùng:', error);
        throw error;  
        // hoặc có thể return error.response.data nếu muốn kiểm tra lỗi từ server
    }
};

export { postCreateNewUser , getAllUsers, putUpdateUser, deleteUser, getAccountByUserid}