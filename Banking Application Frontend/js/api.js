// API Integration Module
class BankingAPI {
    constructor() {
        // Use environment-specific API URL
        this.baseURL = window.location.hostname.includes('github.io')
            ? 'https://your-cloud-backend-url/api'  // Replace with your cloud backend URL
            : 'http://localhost:8080/api';
    }

    // Generic API request method
    async request(endpoint, options = {}) {
        const authHeader = authManager.getAuthHeader();
        if (!authHeader) {
            throw new Error('Not authenticated');
        }

        const config = {
            headers: {
                'Authorization': authHeader,
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };

        try {
            const response = await fetch(`${this.baseURL}${endpoint}`, config);
            
            if (response.status === 401) {
                authManager.logout();
                throw new Error('Authentication failed');
            }

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || `HTTP ${response.status}`);
            }

            // Check if response has content
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            } else {
                return await response.text();
            }
        } catch (error) {
            console.error('API request failed:', error);
            throw error;
        }
    }

    // Customer API methods
    async getCustomers() {
        return await this.request('/customers');
    }

    async getCustomer(customerId) {
        return await this.request(`/customers/${customerId}`);
    }

    async createCustomer(customerData) {
        return await this.request('/customers', {
            method: 'POST',
            body: JSON.stringify(customerData)
        });
    }

    async updateCustomer(customerId, customerData) {
        return await this.request(`/customers/${customerId}`, {
            method: 'PUT',
            body: JSON.stringify(customerData)
        });
    }

    async deleteCustomer(customerId) {
        return await this.request(`/customers/${customerId}`, {
            method: 'DELETE'
        });
    }

    async searchCustomers(name) {
        return await this.request(`/customers/search?name=${encodeURIComponent(name)}`);
    }

    // Account API methods
    async getAccounts() {
        return await this.request('/accounts');
    }

    async getAccount(accountId) {
        return await this.request(`/accounts/${accountId}`);
    }

    async getAccountsByCustomer(customerId) {
        return await this.request(`/accounts/customer/${customerId}`);
    }

    async createAccount(customerId, accountType, initialBalance = 0) {
        const params = new URLSearchParams({
            customerId: customerId,
            accountType: accountType,
            initialBalance: initialBalance
        });
        
        return await this.request(`/accounts?${params.toString()}`, {
            method: 'POST'
        });
    }

    async getAccountBalance(accountId) {
        return await this.request(`/accounts/${accountId}/balance`);
    }

    async activateAccount(accountId) {
        return await this.request(`/accounts/${accountId}/activate`, {
            method: 'PUT'
        });
    }

    async deactivateAccount(accountId) {
        return await this.request(`/accounts/${accountId}/deactivate`, {
            method: 'PUT'
        });
    }

    // Transaction API methods
    async getTransactions() {
        return await this.request('/transactions');
    }

    async getTransaction(transactionId) {
        return await this.request(`/transactions/${transactionId}`);
    }

    async getTransactionsByAccount(accountId) {
        return await this.request(`/transactions/account/${accountId}`);
    }

    async getTransactionsByCustomer(customerId) {
        return await this.request(`/transactions/customer/${customerId}`);
    }

    async deposit(accountId, amount, description = 'Deposit') {
        const params = new URLSearchParams({
            accountId: accountId,
            amount: amount,
            description: description
        });
        
        return await this.request(`/transactions/deposit?${params.toString()}`, {
            method: 'POST'
        });
    }

    async withdraw(accountId, amount, description = 'Withdrawal') {
        const params = new URLSearchParams({
            accountId: accountId,
            amount: amount,
            description: description
        });
        
        return await this.request(`/transactions/withdraw?${params.toString()}`, {
            method: 'POST'
        });
    }

    async transfer(fromAccountId, toAccountId, amount, description = 'Transfer') {
        const params = new URLSearchParams({
            fromAccountId: fromAccountId,
            toAccountId: toAccountId,
            amount: amount,
            description: description
        });
        
        return await this.request(`/transactions/transfer?${params.toString()}`, {
            method: 'POST'
        });
    }
}

// Global API instance
const bankingAPI = new BankingAPI();

// Utility functions for data formatting
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    }).format(date);
}

function formatAccountType(type) {
    switch (type) {
        case 'SAVINGS': return 'Savings';
        case 'CHECKING': return 'Checking';
        case 'BUSINESS': return 'Business';
        default: return type;
    }
}

function formatTransactionType(type) {
    switch (type) {
        case 'DEPOSIT': return 'Deposit';
        case 'WITHDRAWAL': return 'Withdrawal';
        case 'TRANSFER': return 'Transfer';
        case 'PAYMENT': return 'Payment';
        default: return type;
    }
}

function formatStatus(status, type = 'general') {
    let badgeClass = '';
    let displayText = '';

    if (type === 'account') {
        badgeClass = status ? 'status-active' : 'status-inactive';
        displayText = status ? 'Active' : 'Inactive';
    } else if (type === 'transaction') {
        switch (status) {
            case 'COMPLETED':
                badgeClass = 'status-completed';
                displayText = 'Completed';
                break;
            case 'PENDING':
                badgeClass = 'status-pending';
                displayText = 'Pending';
                break;
            case 'FAILED':
                badgeClass = 'status-failed';
                displayText = 'Failed';
                break;
            case 'CANCELLED':
                badgeClass = 'status-failed';
                displayText = 'Cancelled';
                break;
            default:
                badgeClass = 'status-pending';
                displayText = status;
        }
    }

    return `<span class="status-badge ${badgeClass}">${displayText}</span>`;
}

// Error handling utilities
function handleAPIError(error, context = '') {
    console.error(`API Error ${context}:`, error);
    
    let message = 'An unexpected error occurred';
    
    if (error.message) {
        if (error.message.includes('Authentication failed')) {
            message = 'Your session has expired. Please login again.';
        } else if (error.message.includes('Network')) {
            message = 'Network error. Please check your connection.';
        } else {
            message = error.message;
        }
    }
    
    showError(message);
    return message;
}

// Data validation utilities
function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function validatePhoneNumber(phone) {
    const phoneRegex = /^\+?[0-9]{10,15}$/;
    return phoneRegex.test(phone);
}

function validateAmount(amount) {
    const num = parseFloat(amount);
    return !isNaN(num) && num > 0;
}

function validateRequired(value) {
    return value && value.toString().trim().length > 0;
}

// Form data extraction utilities
function getFormData(formElement) {
    const formData = new FormData(formElement);
    const data = {};
    
    for (let [key, value] of formData.entries()) {
        data[key] = value;
    }
    
    return data;
}

function populateForm(formElement, data) {
    Object.keys(data).forEach(key => {
        const element = formElement.querySelector(`[name="${key}"]`);
        if (element) {
            if (element.type === 'checkbox') {
                element.checked = data[key];
            } else {
                element.value = data[key] || '';
            }
        }
    });
}

// Table generation utilities
function createTable(data, columns, actions = []) {
    if (!data || data.length === 0) {
        return '<div class="no-data">No data available</div>';
    }

    let html = '<table class="table">';
    
    // Header
    html += '<thead><tr>';
    columns.forEach(col => {
        html += `<th>${col.header}</th>`;
    });
    if (actions.length > 0) {
        html += '<th>Actions</th>';
    }
    html += '</tr></thead>';
    
    // Body
    html += '<tbody>';
    data.forEach(row => {
        html += '<tr>';
        columns.forEach(col => {
            let value = row[col.field];
            if (col.formatter) {
                value = col.formatter(value, row);
            }
            html += `<td>${value}</td>`;
        });
        
        if (actions.length > 0) {
            html += '<td><div class="action-buttons">';
            actions.forEach(action => {
                html += `<button class="btn-sm ${action.class}" onclick="${action.onclick}(${row[action.idField || 'id']})">${action.text}</button>`;
            });
            html += '</div></td>';
        }
        
        html += '</tr>';
    });
    html += '</tbody></table>';
    
    return html;
} 