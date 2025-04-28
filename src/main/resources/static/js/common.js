/**
 * 電商系統共用 JavaScript 函數庫
 */

// 用戶認證相關功能
const AuthService = {
    /**
     * 檢查用戶是否已登入
     * @returns {Promise} 用戶資料的 Promise 對象
     */
    checkAuth: function() {
        const token = this.getToken();
        if (!token) {
            return Promise.reject(new Error('用戶未登入'));
        }
        
        return fetch('/api/auth/check-auth', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
        .then(response => {
            if (!response.ok) {
                // 令牌無效，清除本地存儲
                this.clearAuthData();
                throw new Error('用戶未登入');
            }
            return response.json();
        });
    },
    
    /**
     * 重定向到登入頁面
     * @param {string} currentPath - 當前頁面路徑，登入後將重定向回此頁面
     */
    redirectToLogin: function(currentPath) {
        window.location.href = '/login?redirect=' + encodeURIComponent(currentPath || window.location.pathname + window.location.search);
    },
    
    /**
     * 從 localStorage 獲取 JWT 令牌
     * @returns {string|null} JWT 令牌或 null
     */
    getToken: function() {
        return localStorage.getItem('token');
    },
    
    /**
     * 清除所有認證相關數據
     */
    clearAuthData: function() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        localStorage.removeItem('tokenExpiry');
    },
    
    /**
     * 登出用戶
     */
    logout: function() {
        this.clearAuthData();
        window.location.href = '/login?logout=true';
    },
    
    /**
     * 更新導航欄顯示
     * 根據用戶登入狀態顯示或隱藏相應的導航元素
     */
    updateNavigation: function() {
        const userElements = document.querySelectorAll('.auth-user');
        const guestElements = document.querySelectorAll('.auth-guest');
        const adminElements = document.querySelectorAll('.auth-admin');
        
        this.checkAuth()
            .then(userData => {
                // 用戶已登入
                // 顯示已登入用戶的元素
                userElements.forEach(el => el.style.display = '');
                // 隱藏未登入用戶的元素
                guestElements.forEach(el => el.style.display = 'none');
                
                // 設置用戶名
                const usernameElements = document.querySelectorAll('.username');
                usernameElements.forEach(el => el.textContent = userData.username || '用戶');
                
                // 檢查用戶是否為管理員
                const isAdmin = userData.roles && userData.roles.includes('ROLE_ADMIN');
                
                // 顯示或隱藏管理員特有的元素
                adminElements.forEach(el => el.style.display = isAdmin ? '' : 'none');
                
                // 更新購物車數量
                CartService.updateCartItemCount();
                
                return userData;
            })
            .catch(error => {
                // 用戶未登入
                // 顯示未登入用戶的元素
                guestElements.forEach(el => el.style.display = '');
                // 隱藏已登入用戶的元素
                userElements.forEach(el => el.style.display = 'none');
                // 隱藏管理員特有的元素
                adminElements.forEach(el => el.style.display = 'none');
            });
    }
};

// 購物車相關功能
const CartService = {
    /**
     * 添加商品到購物車
     * @param {string} productId - 商品 ID
     * @param {number} quantity - 商品數量
     * @returns {Promise} 添加結果的 Promise 對象
     */
    addToCart: function(productId, quantity = 1) {
        return AuthService.checkAuth()
            .catch(error => {
                // 用戶未登入，重定向到登入頁面
                AuthService.redirectToLogin();
                throw error;
            })
            .then(() => {
                // 用戶已登入，添加商品到購物車
                return fetch('/api/cart/items', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + AuthService.getToken()
                    },
                    body: JSON.stringify({
                        productId: productId,
                        quantity: quantity
                    })
                });
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('添加商品到購物車失敗');
                }
                return response.json();
            })
            .then(data => {
                // 更新購物車數量顯示
                this.updateCartItemCount();
                return data;
            });
    },
    
    /**
     * 立即購買商品
     * @param {string} productId - 商品 ID
     * @param {number} quantity - 商品數量
     */
    buyNow: function(productId, quantity = 1) {
        this.addToCart(productId, quantity)
            .then(() => {
                // 跳轉到結帳頁面
                window.location.href = '/checkout';
            })
            .catch(error => {
                console.error('Error:', error);
                // 錯誤處理已在 addToCart 中完成
            });
    },
    
    /**
     * 更新購物車數量顯示
     */
    updateCartItemCount: function() {
        const token = AuthService.getToken();
        if (!token) {
            return Promise.reject(new Error('用戶未登入'));
        }
        
        return fetch('/api/cart', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('獲取購物車資料失敗');
            }
            return response.json();
        })
        .then(data => {
            // 更新購物車數量顯示
            const cartCountElements = document.querySelectorAll('.cart-count');
            if (data.items && data.items.length > 0) {
                cartCountElements.forEach(el => {
                    el.textContent = data.items.length;
                    el.style.display = '';
                });
            } else {
                cartCountElements.forEach(el => el.style.display = 'none');
            }
            return data;
        })
        .catch(error => {
            console.error('Error:', error);
            return null;
        });
    }
};

// UI 相關功能
const UIHelper = {
    /**
     * 顯示提示訊息
     * @param {string} message - 訊息內容
     * @param {string} type - 訊息類型 (success, error, warning, info)
     * @param {number} duration - 顯示時間 (毫秒)
     */
    showMessage: function(message, type = 'info', duration = 3000) {
        // 檢查是否已存在消息容器
        let messageContainer = document.getElementById('message-container');
        
        // 如果不存在，創建一個
        if (!messageContainer) {
            messageContainer = document.createElement('div');
            messageContainer.id = 'message-container';
            messageContainer.style.position = 'fixed';
            messageContainer.style.top = '20px';
            messageContainer.style.right = '20px';
            messageContainer.style.zIndex = '9999';
            document.body.appendChild(messageContainer);
        }
        
        // 創建消息元素
        const messageElement = document.createElement('div');
        messageElement.className = `alert alert-${type} alert-dismissible fade show`;
        messageElement.role = 'alert';
        messageElement.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;
        
        // 添加到容器
        messageContainer.appendChild(messageElement);
        
        // 設置自動關閉
        setTimeout(() => {
            messageElement.classList.remove('show');
            setTimeout(() => {
                messageElement.remove();
            }, 150);
        }, duration);
    },
    
    /**
     * 顯示/隱藏錯誤訊息
     * @param {string} elementId - 錯誤訊息元素 ID
     * @param {string} textElementId - 錯誤文字元素 ID
     * @param {string} message - 錯誤訊息
     * @param {boolean} show - 是否顯示
     */
    toggleErrorMessage: function(elementId, textElementId, message, show) {
        const errorElement = document.getElementById(elementId);
        const textElement = document.getElementById(textElementId);
        
        if (errorElement && textElement) {
            if (show) {
                textElement.textContent = message;
                errorElement.classList.remove('d-none');
            } else {
                errorElement.classList.add('d-none');
            }
        }
    },
    
    /**
     * 顯示/隱藏成功訊息
     * @param {string} elementId - 成功訊息元素 ID
     * @param {string} textElementId - 成功文字元素 ID
     * @param {string} message - 成功訊息
     * @param {boolean} show - 是否顯示
     * @param {number} duration - 自動隱藏時間 (毫秒)
     */
    toggleSuccessMessage: function(elementId, textElementId, message, show, duration = 3000) {
        const successElement = document.getElementById(elementId);
        const textElement = document.getElementById(textElementId);
        
        if (successElement && textElement) {
            if (show) {
                textElement.textContent = message;
                successElement.classList.remove('d-none');
                
                // 自動隱藏
                if (duration > 0) {
                    setTimeout(() => {
                        successElement.classList.add('d-none');
                    }, duration);
                }
            } else {
                successElement.classList.add('d-none');
            }
        }
    },
    
    /**
     * 設置密碼可見性切換
     * @param {string} toggleId - 切換按鈕 ID
     * @param {string} passwordId - 密碼輸入框 ID
     */
    setupPasswordToggle: function(toggleId, passwordId) {
        const toggleButton = document.getElementById(toggleId);
        const passwordInput = document.getElementById(passwordId);
        
        if (toggleButton && passwordInput) {
            toggleButton.addEventListener('click', function() {
                const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordInput.setAttribute('type', type);
                
                // 切換眼睛圖標
                this.querySelector('i').classList.toggle('bi-eye');
                this.querySelector('i').classList.toggle('bi-eye-slash');
            });
        }
    }
};

// 頁面加載時執行的初始化函數
document.addEventListener('DOMContentLoaded', function() {
    // 設置登出按鈕事件
    document.querySelectorAll('.logout-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            AuthService.logout();
        });
    });
    
    // 更新導航欄顯示
    AuthService.updateNavigation();
    
    // 為所有加入購物車按鈕添加事件
    document.querySelectorAll('.add-to-cart').forEach(button => {
        button.addEventListener('click', function() {
            const productId = this.getAttribute('data-product-id');
            
            CartService.addToCart(productId)
                .then(() => {
                    UIHelper.showMessage('商品已成功加入購物車！', 'success');
                })
                .catch(error => {
                    if (error.message !== '用戶未登入') {
                        console.error('Error:', error);
                        UIHelper.showMessage('添加商品失敗，請稍後再試。', 'error');
                    }
                });
        });
    });
});