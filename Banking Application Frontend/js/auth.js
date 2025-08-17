// Authentication Module
class AuthManager {
    constructor() {
        this.baseURL = 'http://localhost:8080/api';
        this.credentials = null;
        this.currentUser = null;
    }

    // Check if user is authenticated
    isAuthenticated() {
        // The source of truth for authentication state between page loads is localStorage.
        // The this.credentials property is just an in-memory cache for the current page.
        return localStorage.getItem('bankingAuth') !== null;
    }

    // Get stored credentials
    getCredentials() {
        if (this.credentials) {
            return this.credentials;
        }
        
        const stored = localStorage.getItem('bankingAuth');
        if (stored) {
            this.credentials = JSON.parse(stored);
            return this.credentials;
        }
        
        return null;
    }

    // Store credentials
    setCredentials(username, password) {
        this.credentials = { username, password };
        this.currentUser = username;
        localStorage.setItem('bankingAuth', JSON.stringify(this.credentials));
    }

    // Clear credentials
    clearCredentials() {
        this.credentials = null;
        this.currentUser = null;
        localStorage.removeItem('bankingAuth');
    }

    // Create Basic Auth header
    getAuthHeader() {
        const creds = this.getCredentials();
        if (!creds) return null;
        
        const auth = btoa(`${creds.username}:${creds.password}`);
        return `Basic ${auth}`;
    }

    // Login function
    async login(username, password) {
        try {
            showLoading(true);
            
            // Test authentication by making a simple API call
            const response = await fetch(`${this.baseURL}/customers`, {
                method: 'GET',
                headers: {
                    'Authorization': `Basic ${btoa(`${username}:${password}`)}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                this.setCredentials(username, password);
                // We'll store the username to infer the role later.
                // In a real app, the API should return the role.
                localStorage.setItem('username', username);
                return { success: true, user: username };
            } else if (response.status === 401) {
                return { success: false, error: 'Invalid username or password' };
            } else {
                return { success: false, error: 'Login failed. Please try again.' };
            }
        } catch (error) {
            console.error('Login error:', error);
            return { success: false, error: 'Network error. Please check your connection.' };
        } finally {
            showLoading(false);
        }
    }

    // Logout function
    logout() {
        this.clearCredentials();
        localStorage.removeItem('username');
        window.location.href = 'index.html';
    }

    // Check authentication on page load
    checkAuth() {
        if (!this.isAuthenticated()) {
            if (window.location.pathname !== '/index.html' && 
                !window.location.pathname.endsWith('index.html') &&
                window.location.pathname !== '/') {
                window.location.href = 'index.html';
            }
            return false;
        }
        
        const creds = this.getCredentials();
        this.currentUser = creds.username;
        return true;
    }

    // Get current user
    getCurrentUser() {
        return localStorage.getItem('username');
    }

    // Get user role
    getUserRole() {
        const username = this.getCurrentUser();
        if (username && username.toLowerCase() === 'admin@bank.com') {
            return 'ADMIN';
        }
        return 'CUSTOMER';
    }

    // Check if user has permission
    hasPermission(requiredRole) {
        const userRole = this.getUserRole();
        
        if (userRole === 'ADMIN') return true;
        if (requiredRole === 'CUSTOMER' && (userRole === 'CUSTOMER' || userRole === 'TELLER')) return true;
        if (requiredRole === 'TELLER' && userRole === 'TELLER') return true;
        
        return false;
    }

    getRole() {
        return this.getUserRole();
    }
}

// Global auth manager instance
const authManager = new AuthManager();

// Login form handler
function handleLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    
    if (!username || !password) {
        showError('Please enter both username and password');
        return;
    }
    
    authManager.login(username, password).then(result => {
        if (result.success) {
            showSuccess('Login successful! Redirecting...');
            setTimeout(() => {
                window.location.href = 'dashboard.html';
            }, 1000);
        } else {
            showError(result.error);
        }
    });
}

// Fill demo credentials
function fillCredentials(username, password) {
    document.getElementById('username').value = username;
    document.getElementById('password').value = password;
}

// Logout function
function logout() {
    if (confirm('Are you sure you want to logout?')) {
        authManager.logout();
    }
}

// Initialize auth on page load
document.addEventListener('DOMContentLoaded', function() {
    // Check if we're on login page
    if (document.getElementById('loginForm')) {
        // On login page
        document.getElementById('loginForm').addEventListener('submit', handleLogin);
        
        // If already authenticated, redirect to dashboard
        if (authManager.isAuthenticated()) {
            window.location.href = 'dashboard.html';
        }
    } else {
        // On other pages, check authentication
        if (!authManager.checkAuth()) {
            return;
        }
        
        // Update current user display
        const currentUserElement = document.getElementById('currentUser');
        if (currentUserElement) {
            const username = authManager.getCurrentUser();
            const role = authManager.getUserRole();
            currentUserElement.textContent = `Welcome, ${username} (${role})`;
        }
    }
});

// Utility functions for UI feedback
function showLoading(show) {
    const spinner = document.getElementById('loadingSpinner');
    if (spinner) {
        if (show) {
            spinner.classList.add('active');
        } else {
            spinner.classList.remove('active');
        }
    }
}

function showError(message) {
    showMessage(message, 'error');
}

function showSuccess(message) {
    showMessage(message, 'success');
}

function showWarning(message) {
    showMessage(message, 'warning');
}

function showMessage(message, type = 'success') {
    // Remove existing messages
    const existingMessages = document.querySelectorAll('.message');
    existingMessages.forEach(msg => msg.remove());
    
    // Create new message
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    messageDiv.textContent = message;
    
    // Add to container
    let container = document.getElementById('messageContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'messageContainer';
        container.className = 'message-container';
        document.body.appendChild(container);
    }
    
    container.appendChild(messageDiv);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (messageDiv.parentNode) {
            messageDiv.remove();
        }
    }, 5000);
} 