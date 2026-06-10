// ── CONFIG ──────────────────────────────────────────────
const API_BASE = '/api';

// ── AUTH ─────────────────────────────────────────────────
const Auth = {
    get() {
        try {
            const user = JSON.parse(localStorage.getItem('hermes_user') || 'null');
            if (!user || !user.id || !user.role) return null;
            return user;
        } catch { return null; }
    },
    save(user) { localStorage.setItem('hermes_user', JSON.stringify(user)); },
    clear()    { localStorage.removeItem('hermes_user'); },
    isLoggedIn() { return !!this.get(); },
    getRole()    { return this.get()?.role || null; },
    getId()      { return this.get()?.id || null; }
};

// ── API ──────────────────────────────────────────────────
async function apiFetch(path, options = {}) {
    const user = Auth.get();
    const headers = { 'Content-Type': 'application/json', ...options.headers };
    if (user) {
        headers['usuarioId'] = user.id;
        headers['role']      = user.role;
    }
    const res = await fetch(API_BASE + path, { ...options, headers });
    if (!res.ok) {
        let msg = `Erro ${res.status}`;
        try { const j = await res.json(); msg = j.message || j.error || msg; } catch {}
        throw new Error(msg);
    }
    const text = await res.text();
    if (!text) return null;
    try { return JSON.parse(text); } catch { return null; }
}
// ── STATUS BADGE ─────────────────────────────────────────
function statusBadge(status) {
    const labels = {
        PENDENTE:    'Pendente',
        ACEITO:      'Aceito',
        AGENDADO:    'Agendado',
        EM_TRANSITO: 'Em Trânsito',
        CONCLUIDO:   'Concluído',
        CANCELADO:   'Cancelado'
    };
    const label = labels[status] || status;
    return `<span class="badge badge-${status.toLowerCase()}">${label}</span>`;
}

// ── FORMAT ───────────────────────────────────────────────
function formatMoney(v) {
    return Number(v).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}
function formatDate(d) {
    if (!d) return '—';
    return new Date(d).toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
}

// ── ALERT ────────────────────────────────────────────────
function showAlert(container, msg, type = 'error') {
    const icons = { success: 'fa-check-circle', error: 'fa-times-circle', info: 'fa-info-circle' };
    container.innerHTML = `
        <div class="alert alert-${type}">
            <i class="fas ${icons[type]}"></i>
            <span>${msg}</span>
        </div>`;
    container.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

// ── MODAL ────────────────────────────────────────────────
function openModal(id) {
    const el = document.getElementById(id);
    if (el) { el.style.display = 'flex'; document.body.style.overflow = 'hidden'; }
}
function closeModal(id) {
    const el = document.getElementById(id);
    if (el) { el.style.display = 'none'; document.body.style.overflow = ''; }
}
// fechar modal ao clicar fora
document.addEventListener('click', e => {
    if (e.target.classList.contains('modal-overlay')) {
        e.target.style.display = 'none';
        document.body.style.overflow = '';
    }
});

// ── NAVBAR ───────────────────────────────────────────────
function buildNavbar(activePage) {
    const user = Auth.get();
    const root = location.pathname.includes('/pages/') ? '../' : '';
    const pages = {
        home: `${root}index.html`,
        clientes: `${root}pages/clientes.html`,
        transportadores: `${root}pages/transportadores.html`,
        fretes: `${root}pages/fretes.html`,
        solicitar: `${root}pages/solicitar-frete.html`,
        disponiveis: `${root}pages/fretes-disponiveis.html`,
        categorias: `${root}pages/categorias.html`,
    };

    const authHtml = user
        ? `<div class="nav-user">
            <button class="nav-user-btn" onclick="toggleDropdown()">
                <i class="fas fa-user-circle"></i>
                <span>${user.nome.split(' ')[0]}</span>
                <i class="fas fa-chevron-down nav-chevron" id="nav-chevron"></i>
            </button>
            <div class="nav-dropdown" id="nav-dropdown" style="display:none">
                <a href="${user.role === 'CLIENTE' ? pages.solicitar : pages.disponiveis}">
                    <i class="fas fa-tachometer-alt"></i> Dashboard
                </a>
                <a href="${pages.fretes}"><i class="fas fa-box"></i> Meus Fretes</a>
                <div class="divider"></div>
                <a href="#" class="logout" onclick="logout()"><i class="fas fa-sign-out-alt"></i> Sair</a>
            </div>
           </div>`
        : `<div class="nav-actions">
            <a href="pages/login.html" class="btn-nav-join">Entrar</a>
            <a href="pages/cadastro.html" class="btn-nav">Criar Conta</a>
           </div>`;

    const navLinks = user?.role === 'CLIENTE'
        ? `<a href="${pages.solicitar}" class="nav-link">Solicitar Frete</a>
       <a href="${pages.fretes}" class="nav-link">Meus Fretes</a>`
        : user?.role === 'TRANSPORTADOR'
            ? `<a href="${pages.disponiveis}" class="nav-link">Fretes Disponíveis</a>
       <a href="${pages.fretes}" class="nav-link">Meus Fretes</a>
       <a href="${pages.categorias}" class="nav-link">Categorias</a>`
            : `<a href="index.html#features" class="nav-link">Como funciona</a>
       <a href="index.html#features" class="nav-link">Vantagens</a>`;

    document.getElementById('navbar').innerHTML = `
        <nav class="navbar">
          <div class="nav-container">
            <a href="${root}index.html" class="nav-logo">
              <span>HERMES</span>
            </a>
            <div class="nav-menu">${navLinks}</div>
            ${authHtml}
          </div>
        </nav>`;
}

function toggleDropdown() {
    const d = document.getElementById('nav-dropdown');
    const c = document.getElementById('nav-chevron');
    if (!d) return;
    const open = d.style.display === 'flex' || d.style.display === 'block';
    d.style.display = open ? 'none' : 'block';
    c?.classList.toggle('open', !open);
}
document.addEventListener('click', e => {
    const user = document.querySelector('.nav-user');
    if (user && !user.contains(e.target)) {
        const d = document.getElementById('nav-dropdown');
        const c = document.getElementById('nav-chevron');
        if (d) d.style.display = 'none';
        c?.classList.remove('open');
    }
});

function logout() {
    Auth.clear();
    const root = location.pathname.includes('/pages/') ? '../' : '';
    location.href = root + 'index.html';
}

// ── FOOTER ───────────────────────────────────────────────
function buildFooter() {
    const el = document.getElementById('footer');
    if (!el) return;
    el.innerHTML = `
        <footer class="footer">
          <div class="footer-content">
            <div class="footer-main">
              <div class="footer-brand">
                <a href="../index.html" class="nav-logo" style="color:#fff;margin-bottom:1rem;display:inline-flex;">
                  <span>HERMES</span>
                </a>
                <p class="footer-description">Conectando clientes e transportadores com eficiência e confiança. Sua plataforma completa para soluções de fretes.</p>
                <div class="footer-social">
                  <a href="#" class="social-link"><i class="fab fa-facebook-f"></i></a>
                  <a href="#" class="social-link"><i class="fab fa-instagram"></i></a>
                  <a href="#" class="social-link"><i class="fab fa-linkedin-in"></i></a>
                </div>
              </div>
              <div class="footer-column">
                <h3>Navegação</h3>
                <ul class="footer-links">
                  <li><a href="../index.html"><i class="fas fa-chevron-right"></i>Página Inicial</a></li>
                  <li><a href="login.html"><i class="fas fa-chevron-right"></i>Entrar</a></li>
                  <li><a href="cadastro.html"><i class="fas fa-chevron-right"></i>Cadastrar</a></li>
                </ul>
              </div>
              <div class="footer-column">
                <h3>Serviços</h3>
                <ul class="footer-links">
                  <li><a href="#"><i class="fas fa-chevron-right"></i>Fretes Urbanos</a></li>
                  <li><a href="#"><i class="fas fa-chevron-right"></i>Fretes Interurbanos</a></li>
                  <li><a href="#"><i class="fas fa-chevron-right"></i>Entregas Rápidas</a></li>
                  <li><a href="#"><i class="fas fa-chevron-right"></i>Cargas Especiais</a></li>
                </ul>
              </div>
              <div class="footer-column">
                <h3>Contato</h3>
                <ul class="footer-contact">
                  <li class="contact-item"><i class="fas fa-envelope"></i><div class="contact-info"><span class="contact-label">E-mail</span><span class="contact-value">contato@hermes.com</span></div></li>
                  <li class="contact-item"><i class="fas fa-clock"></i><div class="contact-info"><span class="contact-label">Horário</span><span class="contact-value">Seg–Sex: 8h–18h</span></div></li>
                </ul>
              </div>
            </div>
            <div class="footer-bottom">
              <div class="footer-copyright"><p>© 2026 Hermes – Sistema de Fretes. Todos os direitos reservados.</p></div>
              <div class="footer-legal">
                <a href="#">Privacidade</a>
                <a href="#">Termos de Uso</a>
              </div>
            </div>
          </div>
        </footer>`;
}
