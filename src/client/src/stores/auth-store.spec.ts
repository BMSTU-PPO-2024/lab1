import { setActivePinia, createPinia } from 'pinia';
import { useAuthStore, TOKEN_KEY } from '@/stores/auth-store';
import { vi, describe, it, beforeEach, expect, afterEach } from 'vitest';
import { jwtDecode } from 'jwt-decode';

describe('Auth Store', () => {
    let authStore: ReturnType<typeof useAuthStore>;
    const mockApi = vi.hoisted(() => {
        return {
            POST: vi.fn(),
        };
    });
    const mockRouter = vi.hoisted(() => {
        return {
            push: vi.fn(),
        };
    });
    const mockJwtDecode = vi.hoisted(() => {
        return vi.fn();
    })
  
    beforeEach(() => {
        setActivePinia(createPinia());
        vi.mock('@/api/api', () => ({ useApi: () => ({ value: mockApi }) }));
        vi.mock('vue-router', () => ({ useRouter: () => mockRouter }));
        vi.mock('jwt-decode', () => ({ jwtDecode: mockJwtDecode }));
        mockJwtDecode.mockImplementation((jwt: string) => jwtDecode(jwt));

        authStore = useAuthStore();
        localStorage.clear();
    });
    afterEach(() => {
        localStorage.removeItem(TOKEN_KEY);
    })

    it('initializes with default values', () => {
        expect(authStore.token).toBeFalsy();
        expect(authStore.isAuthorized).toBe(false);
        expect(authStore.tokenData).toBeNull();
    });

    it('sets token and updates state on successful login', async () => {
        const mockToken = 'mock.jwt.token';
        mockApi.POST.mockResolvedValueOnce({ data: { token: mockToken } });

        await authStore.login({ email: 'test@example.com', password: 'password' });

        expect(authStore.token).toBe(mockToken);
        expect(authStore.isAuthorized).toBe(true);
        expect(localStorage.getItem(TOKEN_KEY)).toBe(mockToken);
    });

    it('throws error on failed login', async () => {
        mockApi.POST.mockResolvedValueOnce({ error: 'Invalid credentials' });

        await expect(
            authStore.login({ email: 'test@example.com', password: 'wrongpassword' })
        ).rejects.toThrow("Can't get token from login: Invalid credentials");

        expect(authStore.token).toBe('');
    });

    it('logs out and clears token', () => {
        authStore.token = 'mock.jwt.token';
        authStore.logout();

        expect(authStore.token).toBeFalsy();
        expect(authStore.isAuthorized).toBe(false);
        expect(mockRouter.push).toHaveBeenCalledWith({ path: '/login' });
    });

    // it('decodes token on token change', () => {
    //     const mockDecodedToken = {
    //         id: '123',
    //         nickname: 'testuser',
    //         email: 'test@example.com',
    //     };
    //     mockJwtDecode.mockImplementationOnce(() => mockDecodedToken);
    //     authStore.token = 'mock.jwt.token';

    //     expect(authStore.tokenData).toEqual(mockDecodedToken);
    // });
});
