import {Navbar} from 'react-bootstrap';
import {Container} from 'react-bootstrap';

function NavHeader(){


    return (
        <div className='header'>
        <Navbar bg="steel" expand="lg">
            <Container>
                <Navbar.Brand href="/self-user">Your user site.</Navbar.Brand>
                <Navbar.Brand href="/new-team">Create new team</Navbar.Brand>
                <Navbar.Brand href="/self-teams">Your teams</Navbar.Brand>
                <Navbar.Brand href="/teams">All teams</Navbar.Brand>
                <Navbar.Brand href="/new-tournament">Create a tournament</Navbar.Brand>
                <Navbar.Brand href="/own-tournaments">Your tournaments</Navbar.Brand>
                <Navbar.Brand href="/tournaments">All tournaments</Navbar.Brand>
            </Container>
        </Navbar>
        </div>
    )

}

export default NavHeader;