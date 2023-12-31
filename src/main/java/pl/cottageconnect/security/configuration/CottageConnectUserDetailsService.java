package pl.cottageconnect.security.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.cottageconnect.security.domain.User;
import pl.cottageconnect.security.service.dao.UserDAO;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CottageConnectUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userDetail = userDAO.findByEmail(username);

        return userDetail.map(CottageConnectUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
}

