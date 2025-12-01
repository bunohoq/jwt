(function() {

    const isAuthenticated = !!sessionStorage.getItem('accessToken');
    const rolesString = sessionStorage.getItem('roles'); // "ROLE_MEMBER,ROLE_ADMIN"
    
    const userRoles = rolesString ? rolesString.split(',') : []; //["ROLE_MEMBER", "ROLE_ADMIN"]

    const isAnonymous = !isAuthenticated;

    const hasRole = (role) => {
        return isAuthenticated && userRoles.includes(role);
    };

    const showAdminLinks = hasRole('ROLE_ADMIN');
    const showMemberLinks = hasRole('ROLE_MEMBER') || hasRole('ROLE_ADMIN');

    const setLinkVisibility = (href, shouldShow) => {
        const link = document.querySelector(`header ul li a[href="${href}"]`);
        
        if (link) {
            link.parentElement.style.display = shouldShow ? 'list-item' : 'none';
        }
    };

    // 규칙 적용
    setLinkVisibility('/', true); 
    setLinkVisibility('/login.html', isAnonymous);
    setLinkVisibility('/join.html', isAnonymous);
    setLinkVisibility('/logout.html', isAuthenticated);
    setLinkVisibility('/member.html', showMemberLinks); 
    setLinkVisibility('/admin.html', showAdminLinks); 

})();