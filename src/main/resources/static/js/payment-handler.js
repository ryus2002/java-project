/**
 * 電子商務系統支付處理模組
 * 安全地處理信用卡和其他支付方式
 */

// 初始化支付處理器
const PaymentProcessor = (function() {
    // 私有變數和方法
    const API_ENDPOINT = '/api/payments';
    const TOKEN_ENDPOINT = '/api/payments/token';
    
    // 支付方式處理器映射
    const paymentHandlers = {
        'CREDIT_CARD': processCreditCardPayment,
        'BANK_TRANSFER': processBankTransferPayment,
        'WALLET': processWalletPayment
    };
    
    // 驗證信用卡號碼 (使用Luhn算法)
    function validateCardNumber(cardNumber) {
        // 移除所有非數字字符
        const digitsOnly = cardNumber.replace(/\D/g, '');
        
        // 檢查長度 (大多數信用卡為13-19位)
        if (digitsOnly.length < 13 || digitsOnly.length > 19) {
            return false;
        }
        
        // Luhn算法實現
        let sum = 0;
        let shouldDouble = false;
        
        // 從右到左處理每個數字
        for (let i = digitsOnly.length - 1; i >= 0; i--) {
            let digit = parseInt(digitsOnly.charAt(i));
            
            if (shouldDouble) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            
            sum += digit;
            shouldDouble = !shouldDouble;
        }
        
        // 如果總和能被10整除，卡號有效
        return (sum % 10) === 0;
    }
    
    // 驗證到期日
    function validateExpiryDate(expiryDate) {
        // 檢查格式 (MM/YY)
        const regex = /^(0[1-9]|1[0-2])\/([0-9]{2})$/;
        if (!regex.test(expiryDate)) {
            return false;
        }
        
        // 解析月份和年份
        const [month, year] = expiryDate.split('/');
        const expMonth = parseInt(month);
        const expYear = parseInt('20' + year); // 轉換為完整年份
        
        // 獲取當前日期
        const now = new Date();
        const currentMonth = now.getMonth() + 1; // getMonth() 返回 0-11
        const currentYear = now.getFullYear();
        
        // 檢查是否過期
        if (expYear < currentYear || (expYear === currentYear && expMonth < currentMonth)) {
            return false;
        }
        
        return true;
    }
    
    // 驗證CVV
    function validateCVV(cvv) {
        // CVV通常是3-4位數字
        return /^[0-9]{3,4}$/.test(cvv);
    }
    
    // 處理信用卡支付
    function processCreditCardPayment(paymentData) {
        return new Promise((resolve, reject) => {
            // 驗證信用卡資訊
            const cardNumber = paymentData.cardNumber.trim();
            const expiryDate = paymentData.expiryDate.trim();
            const cvv = paymentData.cvv.trim();
            const cardholderName = paymentData.cardholderName.trim();
            
            // 執行驗證
            if (!cardholderName) {
                reject(new Error('請輸入持卡人姓名'));
                return;
            }
            
            if (!validateCardNumber(cardNumber)) {
                reject(new Error('信用卡號碼無效'));
                return;
            }
            
            if (!validateExpiryDate(expiryDate)) {
                reject(new Error('到期日無效或已過期'));
                return;
            }
            
            if (!validateCVV(cvv)) {
                reject(new Error('安全碼無效'));
                return;
            }
            
            // 獲取支付令牌 (實際應用中應使用支付閘道API)
            getPaymentToken({
                cardNumber: cardNumber,
                expiryDate: expiryDate,
                cvv: cvv,
                cardholderName: cardholderName
            })
            .then(token => {
                // 返回令牌而非原始信用卡資訊
                resolve({
                    method: 'CREDIT_CARD',
                    token: token,
                    // 僅保留不敏感資訊用於顯示
                    last4: cardNumber.slice(-4),
                    cardType: detectCardType(cardNumber),
                    expiryDate: expiryDate
                });
            })
            .catch(error => {
                reject(new Error('處理信用卡資訊時發生錯誤: ' + error.message));
            });
        });
    }
    
    // 處理銀行轉帳支付
    function processBankTransferPayment(paymentData) {
        return new Promise((resolve, reject) => {
            const bankAccount = paymentData.bankAccount.trim();
            const bankName = paymentData.bankName.trim();
            
            if (!bankAccount || !bankName) {
                reject(new Error('請填寫完整的銀行轉帳資訊'));
                return;
            }
            
            // 驗證銀行帳號格式 (實際應用中應根據具體需求調整)
            if (!/^\d{10,14}$/.test(bankAccount.replace(/[-\s]/g, ''))) {
                reject(new Error('請輸入有效的銀行帳號'));
                return;
            }
            
            resolve({
                method: 'BANK_TRANSFER',
                bankAccount: maskBankAccount(bankAccount),
                bankName: bankName
            });
        });
    }
    
    // 處理電子錢包支付
    function processWalletPayment(paymentData) {
        return new Promise((resolve, reject) => {
            const walletId = paymentData.walletId.trim();
            
            if (!walletId) {
                reject(new Error('請提供電子錢包ID'));
                return;
            }
            
            resolve({
                method: 'WALLET',
                walletId: walletId
            });
        });
    }
    
    // 獲取支付令牌 (模擬)
    function getPaymentToken(cardData) {
        return new Promise((resolve, reject) => {
            // 實際應用中，這裡應該調用支付閘道API
            // 例如 Stripe.js, Braintree 等
            
            // 模擬API請求
            setTimeout(() => {
                try {
                    // 生成隨機令牌 (僅用於示範)
                    const token = 'tok_' + Date.now() + '_' + 
                        Math.random().toString(36).substring(2, 15);
                    resolve(token);
                } catch (error) {
                    reject(new Error('無法生成支付令牌'));
                }
            }, 800); // 模擬網絡延遲
        });
    }
    
    // 檢測信用卡類型
    function detectCardType(cardNumber) {
        // 移除空格和破折號
        const number = cardNumber.replace(/[\s-]/g, '');
        
        // 簡單的卡類型檢測規則
        if (/^4/.test(number)) return 'Visa';
        if (/^(5[1-5]|2[2-7])/.test(number)) return 'MasterCard';
        if (/^3[47]/.test(number)) return 'American Express';
        if (/^6(?:011|5)/.test(number)) return 'Discover';
        if (/^35(?:2[89]|[3-8])/.test(number)) return 'JCB';
        
        return 'Unknown';
    }
    
    // 遮蔽銀行帳號
    function maskBankAccount(accountNumber) {
        const cleaned = accountNumber.replace(/[-\s]/g, '');
        const length = cleaned.length;
        
        if (length <= 4) return cleaned;
        
        // 保留前兩位和後四位，其餘用*替換
        return cleaned.substring(0, 2) + 
               '*'.repeat(length - 6) + 
               cleaned.substring(length - 4);
    }
    
    // 生成唯一交易ID
    function generateTransactionId() {
        return 'txn_' + Date.now() + '_' + 
               Math.random().toString(36).substring(2, 10);
    }
    
    // 公開方法
    return {
        // 處理支付
        processPayment: function(paymentMethod, paymentData, orderId) {
            return new Promise((resolve, reject) => {
                // 檢查支付方式是否支援
                if (!paymentHandlers[paymentMethod]) {
                    reject(new Error('不支援的支付方式'));
                    return;
                }
                
                // 使用對應的處理器處理支付
                paymentHandlers[paymentMethod](paymentData)
                    .then(processedData => {
                        // 生成交易ID
                        const transactionId = generateTransactionId();
                        
                        // 準備提交到後端的資料
                        const paymentRequest = {
                            orderId: orderId,
                            paymentMethod: processedData.method,
                            paymentDetails: processedData,
                            transactionId: transactionId
                        };
                        
                        // 提交支付請求
                        fetch(API_ENDPOINT, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'X-CSRF-TOKEN': getCSRFToken(),
                                'X-Transaction-ID': transactionId
                            },
                            body: JSON.stringify(paymentRequest)
                        })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error(response.status === 400 
                                    ? '支付資料有誤' 
                                    : '支付處理失敗');
                            }
                            return response.json();
                        })
                        .then(data => {
                            // 清除敏感資訊
                            if (processedData.token) {
                                delete processedData.token;
                            }
                            
                            resolve(data);
                        })
                        .catch(error => {
                            reject(error);
                        });
                    })
                    .catch(error => {
                        reject(error);
                    });
            });
        },
        
        // 驗證支付表單
        validatePaymentForm: function(formData) {
            const errors = [];
            const paymentMethod = formData.paymentMethod;
            
            // 基本驗證
            if (!paymentMethod) {
                errors.push('請選擇支付方式');
            }
            
            // 根據支付方式進行特定驗證
            if (paymentMethod === 'CREDIT_CARD') {
                if (!formData.cardholderName) {
                    errors.push('請輸入持卡人姓名');
                }
                
                if (!formData.cardNumber) {
                    errors.push('請輸入信用卡號碼');
                } else if (!validateCardNumber(formData.cardNumber)) {
                    errors.push('信用卡號碼無效');
                }
                
                if (!formData.expiryDate) {
                    errors.push('請輸入到期日');
                } else if (!validateExpiryDate(formData.expiryDate)) {
                    errors.push('到期日無效或已過期');
                }
                
                if (!formData.cvv) {
                    errors.push('請輸入安全碼');
                } else if (!validateCVV(formData.cvv)) {
                    errors.push('安全碼無效');
                }
            } else if (paymentMethod === 'BANK_TRANSFER') {
                if (!formData.bankAccount) {
                    errors.push('請輸入銀行帳號');
                }
                
                if (!formData.bankName) {
                    errors.push('請輸入銀行名稱');
                }
            } else if (paymentMethod === 'WALLET') {
                if (!formData.walletId) {
                    errors.push('請輸入電子錢包ID');
                }
            }
            
            return {
                isValid: errors.length === 0,
                errors: errors
            };
        },
        
        // 格式化信用卡號碼 (添加空格)
        formatCardNumber: function(cardNumber) {
            if (!cardNumber) return '';
            
            // 移除所有非數字字符
            const digitsOnly = cardNumber.replace(/\D/g, '');
            
            // 根據卡類型決定分組方式
            const cardType = detectCardType(digitsOnly);
            
            if (cardType === 'American Express') {
                // AMEX: 4-6-5 格式
                return digitsOnly.replace(/^(\d{4})(\d{6})(\d{5})$/, '$1 $2 $3')
                                 .replace(/^(\d{4})(\d{6})?$/, '$1 $2')
                                 .replace(/^(\d{4})?$/, '$1')
                                 .trim();
            } else {
                // 其他卡: 4-4-4-4 格式
                return digitsOnly.replace(/^(\d{4})(\d{4})(\d{4})(\d{0,4})$/, '$1 $2 $3 $4')
                                 .replace(/^(\d{4})(\d{4})(\d{0,4})$/, '$1 $2 $3')
                                 .replace(/^(\d{4})(\d{0,4})$/, '$1 $2')
                                 .replace(/^(\d{0,4})$/, '$1')
                                 .trim();
            }
        },
        
        // 格式化到期日
        formatExpiryDate: function(input) {
            // 移除非數字字符
            const digitsOnly = input.replace(/\D/g, '');
            
            // 如果長度為0或1，直接返回
            if (digitsOnly.length < 2) return digitsOnly;
            
            // 如果第一位數字大於1，自動添加0前綴
            if (parseInt(digitsOnly[0]) > 1) {
                return `0${digitsOnly[0]}/${digitsOnly.substring(1, 3)}`;
            }
            
            // 如果前兩位數字大於12，調整為12
            if (parseInt(digitsOnly.substring(0, 2)) > 12) {
                return `12/${digitsOnly.substring(2, 4)}`;
            }
            
            // 標準格式化: MM/YY
            return `${digitsOnly.substring(0, 2)}/${digitsOnly.substring(2, 4)}`;
        }
    };
})();

// 獲取CSRF令牌
function getCSRFToken() {
    const metaTag = document.querySelector('meta[name="_csrf"]');
    return metaTag ? metaTag.getAttribute('content') : '';
}

// 顯示載入指示器
function showLoading() {
    // 創建載入指示器元素
    const loadingOverlay = document.createElement('div');
    loadingOverlay.id = 'loading-overlay';
    loadingOverlay.className = 'position-fixed top-0 start-0 w-100 h-100 d-flex justify-content-center align-items-center';
    loadingOverlay.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
    loadingOverlay.style.zIndex = '9999';
    
    loadingOverlay.innerHTML = `
        <div class="spinner-border text-light" role="status">
            <span class="visually-hidden">載入中...</span>
        </div>
    `;
    
    document.body.appendChild(loadingOverlay);
}

// 隱藏載入指示器
function hideLoading() {
    const loadingOverlay = document.getElementById('loading-overlay');
    if (loadingOverlay) {
        document.body.removeChild(loadingOverlay);
    }
}

// 顯示Toast通知
function showToast(message, type = 'success') {
    const toastContainer = document.getElementById('toast-container') || createToastContainer();
    
    const toast = document.createElement('div');
    toast.className = `toast show toast-${type}`;
    toast.setAttribute('role', 'alert');
    toast.setAttribute('aria-live', 'assertive');
    toast.setAttribute('aria-atomic', 'true');
    
    toast.innerHTML = `
        <div class="toast-header">
            <strong class="me-auto">${type === 'success' ? '成功' : '錯誤'}</strong>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">
            ${message}
        </div>
    `;
    
    toastContainer.appendChild(toast);
    
    // 5秒後自動關閉
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => {
            toastContainer.removeChild(toast);
        }, 500);
    }, 5000);
    
    // 點擊關閉按鈕
    toast.querySelector('.btn-close').addEventListener('click', () => {
        toast.classList.remove('show');
        setTimeout(() => {
            toastContainer.removeChild(toast);
        }, 500);
    });
}

// 創建Toast容器
function createToastContainer() {
    const container = document.createElement('div');
    container.id = 'toast-container';
    container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
    document.body.appendChild(container);
    return container;
}