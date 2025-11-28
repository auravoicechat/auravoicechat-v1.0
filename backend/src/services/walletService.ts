/**
 * Wallet Service
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Exchange rate: 30% (diamonds → coins)
 */

interface Balances {
  coins: number;
  diamonds: number;
}

interface ExchangeResult {
  newCoins: number;
  newDiamonds: number;
}

interface Transaction {
  id: string;
  type: string;
  amount: number;
  currency: string;
  description: string;
  timestamp: number;
}

// In-memory storage (use database in production)
const userWallets = new Map<string, Balances>();
const userTransactions = new Map<string, Transaction[]>();

export const getBalances = async (userId: string): Promise<Balances> => {
  return userWallets.get(userId) || { coins: 1500000, diamonds: 250000 };
};

export const exchangeDiamondsToCoins = async (
  userId: string,
  diamonds: number,
  coinsReceived: number
): Promise<ExchangeResult> => {
  const balances = await getBalances(userId);
  
  const newDiamonds = balances.diamonds - diamonds;
  const newCoins = balances.coins + coinsReceived;
  
  userWallets.set(userId, { coins: newCoins, diamonds: newDiamonds });
  
  // Record transaction
  const transactions = userTransactions.get(userId) || [];
  transactions.unshift({
    id: `tx_${Date.now()}`,
    type: 'EXCHANGE',
    amount: coinsReceived,
    currency: 'COINS',
    description: `Exchanged ${diamonds.toLocaleString()} diamonds`,
    timestamp: Date.now()
  });
  userTransactions.set(userId, transactions);
  
  return { newCoins, newDiamonds };
};

export const getTransactions = async (
  userId: string,
  page: number,
  pageSize: number
): Promise<{ transactions: Transaction[]; totalCount: number }> => {
  const allTransactions = userTransactions.get(userId) || [];
  const start = (page - 1) * pageSize;
  
  return {
    transactions: allTransactions.slice(start, start + pageSize),
    totalCount: allTransactions.length
  };
};

export const transferCoins = async (
  fromUserId: string,
  toUserId: string,
  amount: number
): Promise<void> => {
  const fromBalance = await getBalances(fromUserId);
  const toBalance = await getBalances(toUserId);
  
  if (fromBalance.coins < amount) {
    throw new Error('Insufficient balance');
  }
  
  userWallets.set(fromUserId, {
    ...fromBalance,
    coins: fromBalance.coins - amount
  });
  
  userWallets.set(toUserId, {
    ...toBalance,
    coins: toBalance.coins + amount
  });
};
