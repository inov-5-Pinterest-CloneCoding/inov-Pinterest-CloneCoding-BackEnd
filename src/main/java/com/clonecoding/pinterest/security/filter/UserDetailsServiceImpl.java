package com.clonecoding.pinterest.security.filter;
import com.clonecoding.pinterest.user.entity.User;
import com.clonecoding.pinterest.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @NonNull
    private final UserRepository userRepsitory;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        log.info("@@@@@@@@@@@@" + userEmail);
        User user = userRepsitory.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + userEmail));
        return new UserDetailsImpl(user);
    }
}
