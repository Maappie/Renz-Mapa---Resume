/**
 * api-explorer.js — Interactive API Client & Documentation Panel
 */
document.addEventListener('DOMContentLoaded', () => {

    // Initialise Lucide icons
    if (window.lucide) {
        window.lucide.createIcons();
    }

    // Endpoint Database Configuration
    const endpoints = {
        'get-latest': {
            method: 'GET',
            path: '/profile/latest',
            title: 'Get Latest Profile',
            desc: 'Returns the most recently created profile configuration. Used directly on the portfolio homepage.',
            hasParams: false,
            hasBody: false
        },
        'get-all': {
            method: 'GET',
            path: '/profile',
            title: 'List All Profiles',
            desc: 'Fetch a list of all profiles currently residing in the database. Handy for inspecting version history.',
            hasParams: false,
            hasBody: false
        },
        'get-by-id': {
            method: 'GET',
            path: '/profile/{id}',
            title: 'Get Profile by ID',
            desc: 'Fetches a specific profile row by its unique database ID.',
            hasParams: true,
            hasBody: false
        },
        'create': {
            method: 'POST',
            path: '/profile',
            title: 'Create Profile Row',
            desc: 'Inserts a new profile configuration. Previous database entries are archived as history.',
            hasParams: false,
            hasBody: true,
            exampleBody: {
                name: "Renz Mapa",
                title: "Software Engineer",
                email: "renz@example.com",
                phone: "+63 917 123 4567",
                location: "Manila, Philippines",
                bio: "Full Stack Engineer specializing in clean interfaces and scalable APIs.",
                socialLinks: [
                    "https://github.com/renzmapa",
                    "https://linkedin.com/in/renzmapa"
                ]
            }
        },
        'patch-latest': {
            method: 'PATCH',
            path: '/profile/latest',
            title: 'Patch Latest Profile',
            desc: 'Partially updates the most recent profile without needing to identify its specific ID. Pass only the keys you wish to edit.',
            hasParams: false,
            hasBody: true,
            exampleBody: {
                title: "Senior Software Engineer",
                bio: "Experienced developer building modern, resilient web services."
            }
        },
        'patch-by-id': {
            method: 'PATCH',
            path: '/profile/{id}',
            title: 'Patch Profile by ID',
            desc: 'Applies a partial delta update to a specific profile identifier.',
            hasParams: true,
            hasBody: true,
            exampleBody: {
                location: "Cebu City, Philippines"
            }
        },
        'feat-get-all': {
            method: 'GET',
            path: '/features',
            title: 'List All Features',
            desc: 'Returns a summary of all capability toggles, flags, and feature settings.',
            hasParams: false,
            hasBody: false
        },
        'feat-get-by-id': {
            method: 'GET',
            path: '/features/{id}',
            title: 'Get Feature by ID',
            desc: 'Fetch a single active capability toggle by its unique system ID.',
            hasParams: true,
            hasBody: false
        }
    };

    let activeEndpointId = 'get-latest';

    // DOM Elements
    const sidebarNav = document.getElementById('sidebar-nav');
    const endpointSearch = document.getElementById('endpoint-search');
    const panelTitle = document.getElementById('panel-title');
    const panelDesc = document.getElementById('panel-desc');
    const reqMethod = document.getElementById('req-method');
    const reqUrl = document.getElementById('req-url');
    const paramsSection = document.getElementById('params-section');
    const paramId = document.getElementById('param-id');
    const bodySection = document.getElementById('body-section');
    const reqBodyTextarea = document.getElementById('req-body');
    const btnFillExample = document.getElementById('btn-fill-example');
    const btnSend = document.getElementById('btn-send');
    const resStatus = document.getElementById('res-status');
    const resTime = document.getElementById('res-time');
    const resBody = document.getElementById('res-body');
    const responsePanel = document.getElementById('response-panel');

    // ─── NAV GROUP ACCORDIONS ──────────────────────────────────
    document.querySelectorAll('.nav-group-header').forEach(header => {
        header.addEventListener('click', () => {
            const groupItems = header.nextElementSibling;
            header.classList.toggle('active');
            groupItems.classList.toggle('show');
        });
    });

    // ─── SIDEBAR SEARCH ────────────────────────────────────────
    endpointSearch.addEventListener('input', () => {
        const query = endpointSearch.value.toLowerCase().trim();

        document.querySelectorAll('.nav-group').forEach(group => {
            let matchesInGroup = 0;
            const itemsContainer = group.querySelector('.nav-group-items');
            const btns = group.querySelectorAll('.endpoint-btn');

            btns.forEach(btn => {
                const pathText = btn.querySelector('.endpoint-path').textContent.toLowerCase();
                const methodText = btn.querySelector('.method-badge').textContent.toLowerCase();

                if (pathText.includes(query) || methodText.includes(query)) {
                    btn.style.display = 'flex';
                    matchesInGroup++;
                } else {
                    btn.style.display = 'none';
                }
            });

            // Expand group if there are matches, hide group if none
            if (query !== '') {
                if (matchesInGroup > 0) {
                    group.style.display = 'block';
                    itemsContainer.classList.add('show');
                    group.querySelector('.nav-group-header').classList.add('active');
                } else {
                    group.style.display = 'none';
                }
            } else {
                // Reset defaults
                group.style.display = 'block';
                const header = group.querySelector('.nav-group-header');
                if (group.dataset.group === 'profile') {
                    header.classList.add('active');
                    itemsContainer.classList.add('show');
                } else {
                    header.classList.remove('active');
                    itemsContainer.classList.remove('show');
                }
            }
        });
    });

    // ─── SWITCH ACTIVE ENDPOINT ────────────────────────────────
    function selectEndpoint(id) {
        activeEndpointId = id;
        const config = endpoints[id];

        // Update active class in sidebar
        document.querySelectorAll('.endpoint-btn').forEach(btn => {
            btn.classList.toggle('active', btn.dataset.endpoint === id);
        });

        // Set Title & Description
        panelTitle.textContent = config.title;
        panelDesc.textContent = config.desc;

        // Set Request Bar Values
        reqMethod.textContent = config.method;
        reqMethod.className = `method-pill ${config.method.toLowerCase()}`;
        reqUrl.value = config.path;

        // Show/Hide path parameter section
        if (config.hasParams) {
            paramsSection.classList.remove('hidden');
        } else {
            paramsSection.classList.add('hidden');
        }

        // Show/Hide request body section
        if (config.hasBody) {
            bodySection.classList.remove('hidden');
            reqBodyTextarea.value = JSON.stringify(config.exampleBody || {}, null, 4);
        } else {
            bodySection.classList.add('hidden');
            reqBodyTextarea.value = '';
        }

        // Reset Response Panel
        resStatus.textContent = '—';
        resStatus.className = 'status-pill';
        resTime.textContent = '—';
        resBody.textContent = 'Click "Send" to execute the request.';
        resBody.classList.remove('loaded');
    }

    document.querySelectorAll('.endpoint-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            selectEndpoint(btn.dataset.endpoint);
        });
    });

    // ─── FILL EXAMPLE ──────────────────────────────────────────
    btnFillExample.addEventListener('click', () => {
        const config = endpoints[activeEndpointId];
        if (config && config.hasBody) {
            reqBodyTextarea.value = JSON.stringify(config.exampleBody, null, 4);
        }
    });

    // ─── SEND HTTP REQUEST ──────────────────────────────────────
    btnSend.addEventListener('click', async () => {
        const config = endpoints[activeEndpointId];
        let path = config.path;

        // Handle path parameter substitution
        if (config.hasParams) {
            const idVal = paramId.value.trim() || '1';
            path = path.replace('{id}', idVal);
        }

        const absoluteUrl = `/api${path}`;
        const options = {
            method: config.method,
            headers: {}
        };

        // Handle request body
        if (config.hasBody) {
            const bodyContent = reqBodyTextarea.value.trim();
            if (bodyContent) {
                try {
                    // Try parsing JSON to ensure it is valid before sending
                    JSON.parse(bodyContent);
                    options.body = bodyContent;
                    options.headers['Content-Type'] = 'application/json';
                } catch (e) {
                    resStatus.textContent = 'Error';
                    resStatus.className = 'status-pill error';
                    resTime.textContent = '—';
                    resBody.textContent = `Invalid Request Body: Make sure it's valid JSON.\n\nDetail: ${e.message}`;
                    resBody.classList.add('loaded');
                    return;
                }
            }
        }

        resStatus.textContent = 'Sending...';
        resStatus.className = 'status-pill';
        resBody.textContent = 'Waiting for server response...';
        resBody.classList.remove('loaded');

        const startTime = performance.now();

        try {
            const response = await fetch(absoluteUrl, options);
            const endTime = performance.now();
            const elapsed = Math.round(endTime - startTime);

            // Display time elapsed
            resTime.textContent = `${elapsed} ms`;

            // Display status code
            resStatus.textContent = `${response.status} ${response.statusText}`;
            if (response.ok) {
                resStatus.className = 'status-pill success';
            } else {
                resStatus.className = 'status-pill error';
            }

            // Parse response body
            const contentType = response.headers.get('Content-Type');
            let data;

            if (contentType && contentType.includes('application/json')) {
                data = await response.json();
                resBody.textContent = JSON.stringify(data, null, 4);
            } else {
                data = await response.text();
                resBody.textContent = data || 'No response body returned from server.';
            }

            resBody.classList.add('loaded');

        } catch (err) {
            const endTime = performance.now();
            resTime.textContent = `${Math.round(endTime - startTime)} ms`;
            resStatus.textContent = 'Network Error';
            resStatus.className = 'status-pill error';
            resBody.textContent = `Failed to connect to backend server:\n\n${err.message}`;
            resBody.classList.add('loaded');
        }
    });

    // Run setup on first load
    selectEndpoint('get-latest');
});
