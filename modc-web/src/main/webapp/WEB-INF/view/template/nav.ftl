<!-- START OF Logo and Menu Section -->
<div class="site_top">
    <br/>

    <div class="site_top_left">
        <div class="menu_logo">
            <a href="http://monash.edu/eresearch" target="_blank"><img src="${base}/images/logo/logo_merc.png"/></a>
        </div>
    </div>
    <div class="site_top_right">
        <div class="user_login">
            <#if authen_flag?? && authen_flag == 'authenticated'>
                <a href="${base}/user/user_logout.htm">Logout</a>
            <#else>
                <a href="${base}/user/user_login.htm">Login</a> <a href="${base}/user/registration_options.htm">Register</a>
            </#if>
        </div>
    </div>
</div>
<div style="clear: both;"/>
</div>

<!-- Navigations -->
<div class="nav_div">
    <div class="nav_menu">
        <ul>
            <li><a href="${base}/home.htm">Home</a></li>
            <li><a href="#">Projects</a></li>
            <li><a href="${base}/doi/show_mint.htm">DOI</a></li>
            <li><a href="#">Users</a></li>
            <li><a href="#">About Us</a></li>
        </ul>
    </div>
</div>
<div style="clear:both"></div>
<!-- End of Nav -->