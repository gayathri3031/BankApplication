// =================================================================================
// Main Application JavaScript - Consolidated
// All UI logic for the dashboard is in this file.
// =================================================================================

// =================================================================================
// Global State & Navigation
// =================================================================================
let currentSection = 'dashboard';
let cachedData = {
    customers: [],
    accounts: [],
    transactions: []
};

function showSection(sectionName) {
    document.querySelectorAll('.content-section').forEach(section => section.classList.remove('active'));
    document.getElementById(sectionName)?.classList.add('active');
    
    document.querySelectorAll('.nav-menu a').forEach(link => link.classList.remove('active'));
    document.querySelector(`.nav-menu a[onclick="showSection('${sectionName}')"]`)?.classList.add('active');
    
    currentSection = sectionName;
    loadSectionData(sectionName);
}

// =================================================================================
// Data Loading & Display
// =================================================================================
async function loadSectionData(sectionName) {
    try {
        showLoading(true);
        switch (sectionName) {
            case 'dashboard': await loadDashboardData(); break;
            case 'customers': await loadCustomersData(); break;
            case 'accounts': await loadAccountsData(); break;
            case 'transactions': await loadTransactionsData(); break;
        }
    } catch (error) {
        handleAPIError(error, `loading ${sectionName}`);
    } finally {
        showLoading(false);
    }
}

// --- Dashboard ---
async function loadDashboardData() {
    const [customers, accounts, transactions] = await Promise.all([
        bankingAPI.getCustomers().catch(() => []),
        bankingAPI.getAccounts().catch(() => []),
        bankingAPI.getTransactions().catch(() => [])
    ]);
    
    cachedData = { customers, accounts, transactions };

    document.getElementById('totalCustomers').textContent = customers.length;
    const activeAccounts = accounts.filter(acc => acc.isActive);
    document.getElementById('totalAccounts').textContent = activeAccounts.length;
    document.getElementById('totalTransactions').textContent = transactions.length;
    const totalBalance = activeAccounts.reduce((sum, acc) => sum + parseFloat(acc.balance || 0), 0);
    document.getElementById('totalBalance').textContent = formatCurrency(totalBalance);
}

// --- Customers ---
async function loadCustomersData() {
    cachedData.customers = await bankingAPI.getCustomers();
    displayCustomers(cachedData.customers);
}
function displayCustomers(customers) {
    const columns = [
        { field: 'customerId', header: 'ID' },
        { field: 'firstName', header: 'First Name' },
        { field: 'lastName', header: 'Last Name' },
        { field: 'email', header: 'Email' },
        { field: 'phoneNumber', header: 'Phone' },
        { field: 'createdAt', header: 'Created', formatter: (v) => formatDate(v) }
    ];
    const actions = [
        { text: 'View', class: 'btn-primary', onclick: 'viewCustomer', idField: 'customerId' },
        { text: 'Edit', class: 'btn-warning admin-only', onclick: 'editCustomer', idField: 'customerId' },
        { text: 'Delete', class: 'btn-danger admin-only', onclick: 'deleteCustomer', idField: 'customerId' }
    ];
    document.getElementById('customersTable').innerHTML = createTable(customers, columns, actions);
}

// --- Accounts ---
async function loadAccountsData() {
    cachedData.accounts = await bankingAPI.getAccounts();
    displayAccounts(cachedData.accounts);
}
function displayAccounts(accounts) {
    const columns = [
        { field: 'accountId', header: 'ID' },
        { field: 'accountNumber', header: 'Account Number' },
        { field: 'accountType', header: 'Type', formatter: (v) => formatAccountType(v) },
        { field: 'balance', header: 'Balance', formatter: (v) => formatCurrency(v) },
        { field: 'customer', header: 'Customer', formatter: (v) => v ? `${v.firstName} ${v.lastName}` : 'N/A' },
        { field: 'isActive', header: 'Status', formatter: (v) => formatStatus(v, 'account') }
    ];
    const actions = [
        { text: 'View', class: 'btn-primary', onclick: 'viewAccount', idField: 'accountId' },
        { text: 'Toggle Status', class: 'btn-warning admin-only', onclick: 'toggleAccountStatus', idField: 'accountId' }
    ];
    document.getElementById('accountsTable').innerHTML = createTable(accounts, columns, actions);
}

// --- Transactions ---
async function loadTransactionsData() {
    cachedData.transactions = await bankingAPI.getTransactions();
    displayTransactions(cachedData.transactions);
}
function displayTransactions(transactions) {
    const columns = [
        { field: 'transactionId', header: 'ID' },
        { field: 'referenceNumber', header: 'Reference' },
        { field: 'transactionType', header: 'Type', formatter: (v) => formatTransactionType(v) },
        { field: 'amount', header: 'Amount', formatter: (v) => formatCurrency(v) },
        { field: 'status', header: 'Status', formatter: (v) => formatStatus(v, 'transaction') },
        { field: 'transactionDate', header: 'Date', formatter: (v) => formatDate(v) }
    ];
    const actions = [{ text: 'View', class: 'btn-primary', onclick: 'viewTransaction', idField: 'transactionId' }];
    document.getElementById('transactionsTable').innerHTML = createTable(transactions, columns, actions);
}


// =================================================================================
// Modals & Forms
// =================================================================================
function showModal(title, content) {
    document.getElementById('modalTitle').textContent = title;
    document.getElementById('modalBody').innerHTML = content;
    document.getElementById('modalOverlay').classList.add('active');
}
function closeModal() {
    document.getElementById('modalOverlay').classList.remove('active');
}

// --- Customer Forms ---
function showAddCustomerForm() {
    const formHTML = `
        <form id="customerForm">
            <div class="form-group"><label for="firstName">First Name *</label><input type="text" id="firstName" name="firstName" required></div>
            <div class="form-group"><label for="lastName">Last Name *</label><input type="text" id="lastName" name="lastName" required></div>
            <div class="form-group"><label for="email">Email *</label><input type="email" id="email" name="email" required></div>
            <div class="form-group"><label for="phoneNumber">Phone Number</label><input type="tel" id="phoneNumber" name="phoneNumber"></div>
            <div class="form-group"><label for="address">Address</label><textarea id="address" name="address" rows="2"></textarea></div>
            <div class="form-actions">
                <button type="button" class="btn-cancel" onclick="closeModal()">Cancel</button>
                <button type="submit" class="primary-btn">Create Customer</button>
            </div>
        </form>
    `;
    showModal('Add New Customer', formHTML);
    document.getElementById('customerForm').addEventListener('submit', handleCustomerSubmit);
}
function editCustomer(customerId) {
    const customer = cachedData.customers.find(c => c.customerId === customerId);
    const formHTML = `
        <form id="customerForm" data-id="${customerId}">
            <div class="form-group"><label for="firstName">First Name *</label><input type="text" id="firstName" name="firstName" value="${customer.firstName}" required></div>
            <div class="form-group"><label for="lastName">Last Name *</label><input type="text" id="lastName" name="lastName" value="${customer.lastName}" required></div>
            <div class="form-group"><label for="email">Email *</label><input type="email" id="email" name="email" value="${customer.email}" required></div>
            <div class="form-group"><label for="phoneNumber">Phone Number</label><input type="tel" id="phoneNumber" name="phoneNumber" value="${customer.phoneNumber || ''}"></div>
            <div class="form-group"><label for="address">Address</label><textarea id="address" name="address" rows="2">${customer.address || ''}</textarea></div>
            <div class="form-actions">
                <button type="button" class="btn-cancel" onclick="closeModal()">Cancel</button>
                <button type="submit" class="primary-btn">Update Customer</button>
            </div>
        </form>
    `;
    showModal('Edit Customer', formHTML);
    document.getElementById('customerForm').addEventListener('submit', handleCustomerSubmit);
}
async function handleCustomerSubmit(e) {
    e.preventDefault();
    const form = e.target;
    const customerId = form.dataset.id;
    const formData = getFormData(form);

    if (!validateRequired(formData.firstName) || !validateRequired(formData.lastName) || !validateEmail(formData.email)) {
        return showError('Please fill in all required fields with valid data.');
    }
    
    showLoading(true);
    const apiCall = customerId 
        ? bankingAPI.updateCustomer(customerId, formData) 
        : bankingAPI.createCustomer(formData);

    try {
        await apiCall;
        showSuccess(`Customer ${customerId ? 'updated' : 'created'} successfully.`);
        closeModal();
        loadCustomersData();
        loadDashboardData();
    } catch (error) {
        handleAPIError(error, 'submitting customer form');
    } finally {
        showLoading(false);
    }
}

// --- Account Forms ---
function showCreateAccountForm() {
    const formHTML = `
        <form id="accountForm">
            <div class="form-group"><label for="customerId">Customer *</label><select id="customerId" name="customerId" required><option value="">Select Customer</option></select></div>
            <div class="form-group"><label for="accountType">Account Type *</label><select id="accountType" name="accountType" required><option value="">Select Type</option><option value="SAVINGS">Savings</option><option value="CHECKING">Checking</option><option value="BUSINESS">Business</option></select></div>
            <div class="form-group"><label for="initialBalance">Initial Balance</label><input type="number" id="initialBalance" name="initialBalance" value="0" min="0" step="0.01"></div>
            <div class="form-actions"><button type="button" class="btn-cancel" onclick="closeModal()">Cancel</button><button type="submit" class="primary-btn">Create Account</button></div>
        </form>
    `;
    showModal('Create New Account', formHTML);
    loadCustomersForDropdown('customerId');
    document.getElementById('accountForm').addEventListener('submit', handleAccountSubmit);
}
async function handleAccountSubmit(e) {
    e.preventDefault();
    const formData = getFormData(e.target);
    if (!validateRequired(formData.customerId) || !validateRequired(formData.accountType)) {
        return showError('Please select a customer and account type.');
    }

    showLoading(true);
    try {
        await bankingAPI.createAccount(formData.customerId, formData.accountType, formData.initialBalance);
        showSuccess('Account created successfully.');
        closeModal();
        loadAccountsData();
        loadDashboardData();
    } catch (error) {
        handleAPIError(error, 'creating account');
    } finally {
        showLoading(false);
    }
}

// --- Transaction Forms ---
function showTransactionForm(type, accountId = null) {
    const formHTML = `
        <form id="transactionForm" data-type="${type}">
            ${type === 'transfer' ? '<div class="form-group"><label for="fromAccountId">From Account *</label><select id="fromAccountId" name="fromAccountId" required></select></div>' : ''}
            <div class="form-group"><label for="accountId">${type === 'transfer' ? 'To Account' : 'Account'} *</label><select id="accountId" name="accountId" required></select></div>
            <div class="form-group"><label for="amount">Amount *</label><input type="number" id="amount" name="amount" min="0.01" step="0.01" required></div>
            <div class="form-group"><label for="description">Description</label><input type="text" id="description" name="description" placeholder="Optional"></div>
            <div class="form-actions"><button type="button" class="btn-cancel" onclick="closeModal()">Cancel</button><button type="submit" class="primary-btn">Submit ${type.charAt(0).toUpperCase() + type.slice(1)}</button></div>
        </form>
    `;
    showModal(`Make a ${type.charAt(0).toUpperCase() + type.slice(1)}`, formHTML);
    loadAccountsForDropdown('accountId', accountId);
    if (type === 'transfer') loadAccountsForDropdown('fromAccountId');
    document.getElementById('transactionForm').addEventListener('submit', handleTransactionSubmit);
}
async function handleTransactionSubmit(e) {
    e.preventDefault();
    const form = e.target;
    const type = form.dataset.type;
    const formData = getFormData(form);

    if (!validateAmount(formData.amount)) return showError('Invalid amount.');

    showLoading(true);
    try {
        let apiCall;
        if (type === 'deposit') apiCall = bankingAPI.deposit(formData.accountId, formData.amount, formData.description);
        else if (type === 'withdraw') apiCall = bankingAPI.withdraw(formData.accountId, formData.amount, formData.description);
        else if (type === 'transfer') {
            if (formData.fromAccountId === formData.accountId) return showError('From and To accounts cannot be the same.');
            apiCall = bankingAPI.transfer(formData.fromAccountId, formData.accountId, formData.amount, formData.description);
        }
        await apiCall;
        showSuccess('Transaction successful.');
        closeModal();
        loadTransactionsData();
        loadAccountsData();
        loadDashboardData();
    } catch (error) {
        handleAPIError(error, 'submitting transaction');
    } finally {
        showLoading(false);
    }
}


// =================================================================================
// Detailed Views
// =================================================================================
function viewCustomer(customerId) {
    const customer = cachedData.customers.find(c => c.customerId === customerId);
    if (!customer) return showError('Customer not found.');
    
    const content = `
        <div class="view-details">
            <p><strong>ID:</strong> ${customer.customerId}</p>
            <p><strong>Name:</strong> ${customer.firstName} ${customer.lastName}</p>
            <p><strong>Email:</strong> ${customer.email}</p>
            <p><strong>Phone:</strong> ${customer.phoneNumber || 'N/A'}</p>
            <p><strong>Address:</strong> ${customer.address || 'N/A'}</p>
            <p><strong>Member Since:</strong> ${formatDate(customer.createdAt)}</p>
        </div>
    `;
    showModal('Customer Details', content);
}
function viewAccount(accountId) {
    const account = cachedData.accounts.find(a => a.accountId === accountId);
    if (!account) return showError('Account not found.');
    
    const customerName = account.customer ? `${account.customer.firstName} ${account.customer.lastName}` : 'N/A';
    const content = `
        <div class="view-details">
            <p><strong>Account ID:</strong> ${account.accountId}</p>
            <p><strong>Account #:</strong> ${account.accountNumber}</p>
            <p><strong>Owner:</strong> ${customerName}</p>
            <p><strong>Type:</strong> ${formatAccountType(account.accountType)}</p>
            <p><strong>Balance:</strong> <span class="balance">${formatCurrency(account.balance)}</span></p>
            <p><strong>Status:</strong> ${formatStatus(account.isActive, 'account')}</p>
            <p><strong>Created On:</strong> ${formatDate(account.createdAt)}</p>
        </div>
        <div class="modal-actions">
            <button class="primary-btn admin-only" onclick="showTransactionForm('deposit', ${accountId})">Deposit</button>
            <button class="primary-btn admin-only" onclick="showTransactionForm('withdraw', ${accountId})">Withdraw</button>
        </div>
    `;
    showModal('Account Details', content);
}
function viewTransaction(transactionId) {
    const transaction = cachedData.transactions.find(t => t.transactionId === transactionId);
    if (!transaction) return showError('Transaction not found.');

    const fromAccount = transaction.fromAccount ? transaction.fromAccount.accountNumber : 'N/A';
    const toAccount = transaction.toAccount ? transaction.toAccount.accountNumber : 'N/A';
    
    const content = `
        <div class="view-details">
            <p><strong>Transaction ID:</strong> ${transaction.transactionId}</p>
            <p><strong>Reference #:</strong> ${transaction.referenceNumber}</p>
            <p><strong>Type:</strong> ${formatTransactionType(transaction.transactionType)}</p>
            <p><strong>Amount:</strong> <span class="balance">${formatCurrency(transaction.amount)}</span></p>
            <p><strong>From Account:</strong> ${fromAccount}</p>
            <p><strong>To Account:</strong> ${toAccount}</p>
            <p><strong>Description:</strong> ${transaction.description || 'N/A'}</p>
            <p><strong>Date:</strong> ${formatDate(transaction.transactionDate)}</p>
            <p><strong>Status:</strong> ${formatStatus(transaction.status, 'transaction')}</p>
        </div>
    `;
    showModal('Transaction Details', content);
}


// =================================================================================
// Filtering and Searching
// =================================================================================
function searchCustomers() {
    const searchTerm = document.getElementById('customerSearch')?.value.toLowerCase();
    if (searchTerm === undefined) return;
    const filtered = cachedData.customers.filter(c =>
        c.firstName.toLowerCase().includes(searchTerm) ||
        c.lastName.toLowerCase().includes(searchTerm) ||
        c.email.toLowerCase().includes(searchTerm)
    );
    displayCustomers(filtered);
}

function filterAccounts() {
    const type = document.getElementById('accountTypeFilter')?.value;
    const status = document.getElementById('accountStatusFilter')?.value;
    if (type === undefined || status === undefined) return;
    
    let filtered = cachedData.accounts;
    if (type) {
        filtered = filtered.filter(a => a.accountType === type);
    }
    if (status) {
        filtered = filtered.filter(a => a.isActive.toString() === status);
    }
    displayAccounts(filtered);
}

function filterTransactions() {
    const type = document.getElementById('transactionTypeFilter')?.value;
    const from = document.getElementById('dateFrom')?.value;
    const to = document.getElementById('dateTo')?.value;
    if (type === undefined || from === undefined || to === undefined) return;

    let filtered = cachedData.transactions;
    if (type) {
        filtered = filtered.filter(tx => tx.transactionType === type);
    }
    if (from) {
        filtered = filtered.filter(tx => new Date(tx.transactionDate) >= new Date(from));
    }
    if (to) {
        const toDate = new Date(to);
        toDate.setHours(23, 59, 59, 999);
        filtered = filtered.filter(tx => new Date(tx.transactionDate) <= toDate);
    }
    displayTransactions(filtered);
}


// =================================================================================
// Event Handlers & Initialization
// =================================================================================
function deleteCustomer(customerId) {
    if (confirm('Are you sure? This will also delete all associated accounts.')) {
        showLoading(true);
        bankingAPI.deleteCustomer(customerId)
            .then(() => { showSuccess('Customer deleted.'); loadCustomersData(); loadDashboardData(); })
            .catch(error => handleAPIError(error, 'deleting customer'))
            .finally(() => showLoading(false));
    }
}
function toggleAccountStatus(accountId, newStatus) {
    const account = cachedData.accounts.find(a => a.accountId === accountId);
    const action = newStatus ? 'activate' : 'deactivate';
    if (confirm(`Are you sure you want to ${action} this account?`)) {
        showLoading(true);
        const apiCall = newStatus ? bankingAPI.activateAccount(accountId) : bankingAPI.deactivateAccount(accountId);
        apiCall
            .then(() => { showSuccess(`Account ${action}d.`); loadAccountsData(); loadDashboardData(); })
            .catch(error => handleAPIError(error, `toggling account status`))
            .finally(() => showLoading(false));
    }
}
async function loadCustomersForDropdown(selectId) {
    const select = document.getElementById(selectId);
    if (!select) return;
    try {
        const customers = await bankingAPI.getCustomers();
        select.innerHTML = '<option value="">Select Customer</option>';
        customers.forEach(c => select.innerHTML += `<option value="${c.customerId}">${c.firstName} ${c.lastName}</option>`);
    } catch (e) {
        select.innerHTML = '<option value="">Error loading customers</option>';
    }
}
async function loadAccountsForDropdown(selectId, selectedId) {
    const select = document.getElementById(selectId);
    if (!select) return;
    try {
        const accounts = await bankingAPI.getAccounts();
        select.innerHTML = `<option value="">Select Account</option>`;
        accounts.filter(a => a.isActive).forEach(a => select.innerHTML += `<option value="${a.accountId}" ${a.accountId === selectedId ? 'selected' : ''}>${a.accountNumber} (${formatCurrency(a.balance)})</option>`);
    } catch (e) {
        select.innerHTML = '<option value="">Error loading accounts</option>';
    }
}

// =================================================================================
// Page Initialization
// =================================================================================
document.addEventListener('DOMContentLoaded', () => {
    // Router to initialize the correct page
    const page = document.body.dataset.page;

    if (page === 'dashboard') {
        // Authenticate and initialize the dashboard
        if (!authManager.checkAuth()) return; // Redirects if not authenticated

        // Set welcome message
        const currentUserElement = document.getElementById('currentUser');
        if (currentUserElement) {
            const username = authManager.getCurrentUser();
            const role = authManager.getUserRole();
            currentUserElement.textContent = `Welcome, ${username} (${role})`;
        }

        // Add role-specific class for CSS-based UI toggling
        document.body.classList.add(`role-${authManager.getUserRole().toLowerCase()}`);

        // Hook up common event listeners
        const modalOverlay = document.getElementById('modalOverlay');
        if (modalOverlay) {
            modalOverlay.addEventListener('click', (e) => { if (e.target === modalOverlay) closeModal(); });
            document.addEventListener('keydown', (e) => { if (e.key === 'Escape') closeModal(); });
        }

        // Hook up search and filter events
        document.getElementById('customerSearch')?.addEventListener('input', searchCustomers);
        document.getElementById('accountTypeFilter')?.addEventListener('change', filterAccounts);
        document.getElementById('accountStatusFilter')?.addEventListener('change', filterAccounts);
        document.getElementById('transactionTypeFilter')?.addEventListener('change', filterTransactions);
        document.getElementById('dateFrom')?.addEventListener('change', filterTransactions);
        document.getElementById('dateTo')?.addEventListener('change', filterTransactions);

        // Set max date for date pickers to today
        const today = new Date().toISOString().split('T')[0];
        const dateFrom = document.getElementById('dateFrom');
        const dateTo = document.getElementById('dateTo');
        if (dateFrom) dateFrom.max = today;
        if (dateTo) dateTo.max = today;

        // Initial data load
        loadSectionData('dashboard');
    }
}); 